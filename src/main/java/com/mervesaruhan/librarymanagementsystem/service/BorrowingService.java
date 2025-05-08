package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BorrowingMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.repository.BorrowingRepository;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LogHelper logHelper;


    public BorrowingDto saveBorrowing(BorrowingSaveRequestDto saveRequestDto){

        logHelper.info("Creating borrowing: userId={}, bookId={}", saveRequestDto.userId(), saveRequestDto.bookId());

        //Kullanıcı  ve kitap kontrolü: sistemde böyle bir kullanıcı/kitap var mı ? Varsa active/ envanteri yeterli  durumda mı?
        User user = userRepository.findById(saveRequestDto.userId())
                .orElseThrow(() -> new InvalidUserIdException(saveRequestDto.userId()));

        Book book = bookRepository.findById(saveRequestDto.bookId())
                .orElseThrow(() -> new InvalidBookIdException(saveRequestDto.bookId()));

        if (!user.getActive()) {
            logHelper.warn("Inactive user attempted to borrow. userId={}", user.getId());
            throw new IllegalArgumentException("The user is currently inactive.");
        }

        if (book.getInventoryCount() == null || book.getInventoryCount() <= 0) {
            logHelper.warn("Book out of stock. bookId={}", book.getId());
            throw new IllegalArgumentException("Book stock is insufficient.");
        }

        // borrowing oluşturma(ödünç alma tarihi ve teslim tarihi oluşturulur)
        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setUser(user);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(14));
        borrowing.setStatus(BORROWED);

        //book envanteri güncelleme
        book.setInventoryCount(book.getInventoryCount() - 1);
        bookRepository.save(book);

        // borrowing kaydetme
        borrowingRepository.save(borrowing);

        // eligibility kontrolü (max 5 kitap veya gecikme kontrolü)
        userService.checkUserEligibilityAndUpdateStatus(user.getId(), true);

        logHelper.info("Borrowing created successfully. borrowingId={}, userId={}, bookId={}", borrowing.getId(),
                user.getId(), book.getId());

        return borrowingMapper.toBorrowingDto(borrowing);

    }


    public BorrowingDto returnBook(Long borrowingId){

        logHelper.info("Return process started. borrowingId={}", borrowingId);

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException("There is no borrowing record for the given  ID: " + borrowingId));

        if (borrowing.getStatus() == RETURNED) {
            logHelper.warn("Return attempted for already returned book. borrowingId={}", borrowingId);
            throw new IllegalStateException("This book has already been returned.");
        }
        borrowing.setStatus(RETURNED);
        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setInventoryCount(book.getInventoryCount() + 1);

        bookRepository.save(book);
        borrowingRepository.save(borrowing);
        logHelper.info("Return successful. borrowingId={}", borrowingId);

        return borrowingMapper.toBorrowingDto(borrowing);
    }


    public List<BorrowingDto> getMyBorrowingHistory() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logHelper.debug("Fetching borrowing list from DB for username: {}", username);

        List<Borrowing> borrowingList = borrowingRepository.findAllByUserUsername(username);
        return borrowingMapper.toBorrowingDtoList(borrowingList);
    }


    public List<BorrowingDto> getAllBorrowingHistory() {
        logHelper.debug("Retrieving all borrowings from DB");
        List<Borrowing> borrowings = borrowingRepository.findAll();
        return borrowingMapper.toBorrowingDtoList(borrowings);
    }


    public List<BorrowingDto> getOverdueBorrowings() {

        logHelper.info("Checking for overdue borrowings");
        LocalDate today = LocalDate.now();

        final List<Borrowing> overdueList = borrowingRepository
                .findByDueDateBeforeAndStatus(today, BORROWED)
                .stream()
                .peek(b -> b.setStatus(OVERDUE))
                .toList();

        borrowingRepository.saveAll(overdueList);

        logHelper.info("Found {} overdue borrowings", overdueList.size());
        return borrowingMapper.toBorrowingDtoList(overdueList);
    }


    public String generateOverdueReport() {
        List<BorrowingDto> overdueList = getOverdueBorrowings();

        int totalBooks = (int) bookRepository.count();
        int totalBorrowed = borrowingRepository.countByStatus(BORROWED);
        int totalReturned = borrowingRepository.countByStatus(RETURNED);
        int totalOverdue = borrowingRepository.countByStatus(OVERDUE);

        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("""
            📚 LIBRARY OVERDUE REPORT
            --------------------------
            Total Books        : %d
            Currently Borrowed : %d
            Returned           : %d
            Overdue            : %d
            """.formatted(totalBooks, totalBorrowed, totalReturned, totalOverdue));

        reportBuilder.append("\n\n Overdue Book Details:\n");

        overdueList.forEach(b -> reportBuilder.append(String.format(
                " - Book: %s [Book ID: %d] | Borrowed by: %s [User ID: %d] | Due: %s\n",
                b.bookTitle(),
                b.bookId(),
                b.userFullName(),
                b.userId(),
                b.dueDate()
        )));

        reportBuilder.append("\nReport generated at: ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        logHelper.info("Overdue report generated at {} with {} records",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                overdueList.size());
        return reportBuilder.toString();
    }

}
