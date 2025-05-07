package com.mervesaruhan.librarymanagementsystem.kafka.error;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "logTransactionManager")
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

}
