package com.mervesaruhan.librarymanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BORROWINGS")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrowing")
    @SequenceGenerator(name = "borrowing", sequenceName = "BORROWING_ID_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BORROW_DATE", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "DUE_DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "RETURN_DATE")
    @Builder.Default
    private LocalDate returnDate = null;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BorrowingStatusEnum status = BorrowingStatusEnum.BORROWED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="BOOK_ID",nullable = false)
    @JsonBackReference
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_ID", nullable = false)
    @JsonBackReference
    private User user;


}
