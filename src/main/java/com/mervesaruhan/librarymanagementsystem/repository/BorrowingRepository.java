package com.mervesaruhan.librarymanagementsystem.repository;

import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findAllByUserId(Long userId);
    List<Borrowing> findByDueDateBeforeAndStatus(LocalDate date, BorrowingStatusEnum status);
    int countByStatus(BorrowingStatusEnum status);

    List<Borrowing> findAllByUserUsername(String username);
}
