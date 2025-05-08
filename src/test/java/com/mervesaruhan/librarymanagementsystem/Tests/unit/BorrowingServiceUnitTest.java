package com.mervesaruhan.librarymanagementsystem.Tests.unit;


import com.mervesaruhan.librarymanagementsystem.general.BookTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.general.BorrowingTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.general.UserTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BorrowingMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.repository.BorrowingRepository;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import com.mervesaruhan.librarymanagementsystem.Tests.BorrowingService;
import com.mervesaruhan.librarymanagementsystem.Tests.UserService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @Mock
    private LogHelper logHelper;

    @Mock
    private UserService userService;

    @InjectMocks
    private BorrowingService borrowingService;

    @InjectMocks
    private BorrowingService borrowingServiceTest;

    private User user;
    private Book book;
    private Borrowing borrowing;
    private BorrowingDto borrowingDto;
    private List<Borrowing> borrowingList;

    @BeforeEach
    void setUp() {
        user = UserTestDataGenerator.createUserLibrarian();
        book = BookTestDataGenerator.createBook();
        borrowing = BorrowingTestDataGenerator.createBorrowing();
        borrowingDto = BorrowingTestDataGenerator.createBorrowingBookDto();
        borrowingList = BorrowingTestDataGenerator.createBorrowingList();
    }




    @Test
    void shouldSaveBorrowingSuccessfully() {
        BorrowingSaveRequestDto requestDto = new BorrowingSaveRequestDto(1L, 1L);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(borrowingMapper.toBorrowingDto(any(Borrowing.class))).thenReturn(borrowingDto);


        when(userService.checkUserEligibilityAndUpdateStatus(user.getId(), true))
                .thenReturn(UserTestDataGenerator.createUserDto());

        BorrowingDto result = borrowingService.saveBorrowing(requestDto);

        assertThat(result).isEqualTo(borrowingDto);
        verify(bookRepository).save(book);
        verify(borrowingRepository).save(any(Borrowing.class));
        verify(userService).checkUserEligibilityAndUpdateStatus(user.getId(), true);
    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        BorrowingSaveRequestDto requestDto = new BorrowingSaveRequestDto(999L, 1L);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.saveBorrowing(requestDto))
                .isInstanceOf(InvalidUserIdException.class);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        BorrowingSaveRequestDto requestDto = new BorrowingSaveRequestDto(1L, 999L);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.saveBorrowing(requestDto))
                .isInstanceOf(InvalidBookIdException.class);
    }

    @Test
    void shouldThrowExceptionWhenUserIsInactive() {
        user.setActive(false);
        BorrowingSaveRequestDto requestDto = new BorrowingSaveRequestDto(1L, 1L);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> borrowingService.saveBorrowing(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The user is currently inactive");
    }

    @Test
    void shouldThrowExceptionWhenBookInventoryIsInsufficient() {
        book.setInventoryCount(0);

        BorrowingSaveRequestDto requestDto = new BorrowingSaveRequestDto(1L, 1L);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> borrowingService.saveBorrowing(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Book stock is insufficient");
    }

    @Test
    void shouldReturnBookSuccessfully() {
        book.setInventoryCount(TestConstants.TEST_BOOK_INVENTORY_COUNT);
        borrowing.setBook(book);
        borrowing.setStatus(BorrowingStatusEnum.BORROWED);

        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(borrowingMapper.toBorrowingDto(any(Borrowing.class))).thenReturn(borrowingDto);

        BorrowingDto result = borrowingService.returnBook(1L);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();
        assertThat(savedBook.getInventoryCount()).isEqualTo(TestConstants.TEST_BOOK_INVENTORY_COUNT + 1);

        verify(borrowingRepository).save(any(Borrowing.class));
        verify(borrowingMapper).toBorrowingDto(any(Borrowing.class));
        assertThat(result).isEqualTo(borrowingDto);
    }

    @Test
    void shouldThrowExceptionIfBorrowingNotFound() {
        when(borrowingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.returnBook(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("There is no borrowing record for the given  ID");
    }

    @Test
    void shouldThrowExceptionIfBookAlreadyReturned() {
        borrowing.setStatus(BorrowingStatusEnum.RETURNED);
        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));

        assertThatThrownBy(() -> borrowingService.returnBook(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already been returned");
    }

    @Test
    void shouldReturnMyBorrowingHistorySuccessfully() {
        String username = "testUser";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(borrowingRepository.findAllByUserUsername(username)).thenReturn(borrowingList);
        when(borrowingMapper.toBorrowingDtoList(borrowingList)).thenReturn(List.of(borrowingDto));

        List<BorrowingDto> result = borrowingService.getMyBorrowingHistory();

        assertThat(result).hasSize(1).containsExactly(borrowingDto);
        verify(borrowingRepository).findAllByUserUsername(username);
        verify(borrowingMapper).toBorrowingDtoList(borrowingList);
    }

    @Test
    void shouldReturnAllBorrowingHistorySuccessfully() {
        when(borrowingRepository.findAll()).thenReturn(borrowingList);
        when(borrowingMapper.toBorrowingDtoList(borrowingList)).thenReturn(List.of(borrowingDto));

        List<BorrowingDto> result = borrowingService.getAllBorrowingHistory();

        assertThat(result).hasSize(1).containsExactly(borrowingDto);
        verify(borrowingRepository).findAll();
        verify(borrowingMapper).toBorrowingDtoList(borrowingList);
    }

    @Test
    void shouldReturnOverdueBorrowingsSuccessfully() {
        List<Borrowing> overdueBorrowings = List.of(borrowing);
        when(borrowingRepository.findByDueDateBeforeAndStatus(any(LocalDate.class), eq(BorrowingStatusEnum.BORROWED)))
                .thenReturn(overdueBorrowings);
        when(borrowingMapper.toBorrowingDtoList(overdueBorrowings)).thenReturn(List.of(borrowingDto));

        List<BorrowingDto> result = borrowingService.getOverdueBorrowings();

        assertThat(result).hasSize(1).containsExactly(borrowingDto);
        verify(borrowingRepository).findByDueDateBeforeAndStatus(any(LocalDate.class), eq(BorrowingStatusEnum.BORROWED));
        verify(borrowingMapper).toBorrowingDtoList(overdueBorrowings);
        assertThat(borrowing.getStatus()).isEqualTo(BorrowingStatusEnum.OVERDUE);
        verify(borrowingRepository).saveAll(overdueBorrowings);
    }

    @Test
    void shouldThrowExceptionWhenNoBorrowingFoundForUser() {
        doNothing().when(logHelper).debug(anyString(), (Object[]) any());

        String username = "nonExistingUser";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(borrowingRepository.findAllByUserUsername(username)).thenReturn(Collections.emptyList());

        List<BorrowingDto> result = borrowingService.getMyBorrowingHistory();

        assertThat(result).isEmpty();
        verify(borrowingRepository).findAllByUserUsername(username);
    }

    @Test
    void shouldGenerateOverdueReportSuccessfully() {
        List<BorrowingDto> overdueList = BorrowingTestDataGenerator.createBorrowingDtoList();

        when(borrowingRepository.countByStatus(BorrowingStatusEnum.BORROWED)).thenReturn(5);
        when(borrowingRepository.countByStatus(BorrowingStatusEnum.RETURNED)).thenReturn(10);
        when(borrowingRepository.countByStatus(BorrowingStatusEnum.OVERDUE)).thenReturn(2);
        when(bookRepository.count()).thenReturn(50L);

        when(borrowingServiceTest.getOverdueBorrowings()).thenReturn(overdueList);

        String report = borrowingServiceTest.generateOverdueReport();

        assertThat(report).contains("LIBRARY OVERDUE REPORT");
        assertThat(report).contains("Total Books        : 50");
        assertThat(report).contains("Currently Borrowed : 5");
        assertThat(report).contains("Returned           : 10");
        assertThat(report).contains("Overdue            : 2");
        assertThat(report).contains("Overdue Book Details:");
        assertThat(report).contains(TestConstants.TEST_BOOK_TITLE);
        assertThat(report).contains(TestConstants.TEST_USER_NAME);
        assertThat(report).contains("Report generated at:");
    }
}

