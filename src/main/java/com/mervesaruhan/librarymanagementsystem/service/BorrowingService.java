package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.OverdueReportDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BorrowingMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.repository.BorrowingRepository;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum.*;

@Service
@RequiredArgsConstructor
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;


    public BorrowingDto saveBorrowing(BorrowingSaveRequestDto saveRequestDto){

        //Kullanıcı  ve kitap kontrolü: sistemde böyle bir kullanıcı/kitap var mı ? Varsa active/ envanteri yeterli  durumda mı?
        User user = userRepository.findById(saveRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("Girilen ID'de kullanıcı yoktur."));

        Book book = bookRepository.findById(saveRequestDto.bookId())
                .orElseThrow(() -> new IllegalArgumentException("Girilen ID'de kitap yoktur."));

        if (!user.getActive()) {
            throw new IllegalArgumentException("Kullanıcı pasif durumdadır.");
        }

        if (book.getInventoryCount() == null || book.getInventoryCount() <= 0) {
            throw new IllegalArgumentException("Kitap envanteri yetersiz.");
        }

        // borrowing oluşturma
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

        return borrowingMapper.toBorrowingDto(borrowing);

    }


    public BorrowingDto returnBook(Long borrowingId){
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'ye ait ödünç alma yoktur. ID: " + borrowingId));

        if (borrowing.getStatus() == RETURNED) {
            throw new IllegalStateException("Bu kitap zaten iade edilmiş.");
        }
        borrowing.setStatus(RETURNED);
        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setInventoryCount(book.getInventoryCount() + 1);

        bookRepository.save(book);
        borrowingRepository.save(borrowing);

        return borrowingMapper.toBorrowingDto(borrowing);
    }


    public List<BorrowingDto> getBorrowingHistoryByUser(Long userId){

        if(!userRepository.existsById(userId)){
            throw  new EntityNotFoundException("ID " + userId + " ile kullanıcı bulunamadı.");
        }
        List<Borrowing> borrowingList = borrowingRepository.findAllByUserId(userId);
        return borrowingMapper.toBorrowingDtoList(borrowingList);
    }


    public List<BorrowingDto> getAllBorrowingHistory() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        return borrowingMapper.toBorrowingDtoList(borrowings);
    }


    public List<BorrowingDto> getOverdueBorrowings() {
        LocalDate today = LocalDate.now();

        List<Borrowing> overdueList = borrowingRepository
                .findByDueDateBeforeAndStatus(today, BORROWED);

        // Overdue olanların durumunu güncelle - takip için
        for(Borrowing b : overdueList)
            b.setStatus(OVERDUE);

        borrowingRepository.saveAll(overdueList);
        return borrowingMapper.toBorrowingDtoList(overdueList);
    }


    public OverdueReportDto getOverdueReport() {
        List<BorrowingDto> overdueList = getOverdueBorrowings();

        int totalBooks = (int) bookRepository.count();
        int totalBorrowed = borrowingRepository.countByStatus(BORROWED);
        int totalReturned = borrowingRepository.countByStatus(RETURNED);
        int totalOverdue = borrowingRepository.countByStatus(OVERDUE);

        return new OverdueReportDto(totalBooks, totalBorrowed, totalReturned, totalOverdue, overdueList);
    }

}
