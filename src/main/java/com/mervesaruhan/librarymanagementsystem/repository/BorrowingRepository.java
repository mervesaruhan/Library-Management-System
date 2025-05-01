package com.mervesaruhan.librarymanagementsystem.repository;

import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
}
