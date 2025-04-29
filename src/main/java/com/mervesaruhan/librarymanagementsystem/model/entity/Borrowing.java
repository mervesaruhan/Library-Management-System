package com.mervesaruhan.librarymanagementsystem.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BORROWINGS")
public class Borrowing {
    @Id
    private Long id;

}
