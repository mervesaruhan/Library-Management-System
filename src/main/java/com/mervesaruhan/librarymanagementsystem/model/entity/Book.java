package com.mervesaruhan.librarymanagementsystem.model.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BOOKS")
@ToString(exclude = "borrowedList" )
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book")
    @SequenceGenerator(name="book", sequenceName = "BOOK_ID_SEQ", allocationSize=1)
    @Column(name = "ID")
    private  Long id;

    @Column(name ="TITLE" , nullable = false)
    private String title;

    @Column(name ="AUTHOR" , nullable = false)
    private String author;

    @Column(name ="ISBN" , nullable = false)
    private String isbn;

    @Column(name ="PUBLISHER" , nullable = false)
    private String publisher;

    @Column(name ="PUBLISHEDDATE" , nullable = false)
    private LocalDate publishedDate;

    @Column(name ="GENRE" , nullable = false)
    private String genre;

    @Column(name ="DESCRIPTION")
    private String description;

    @Column(name ="PAGE_COUNT")
    private Integer pageCount;

    @Column(name="INVENTORY_COUNT")
    @Builder.Default
    private Integer inventoryCount= 0;


    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Borrowing> borrowedList = new ArrayList<>();


}
