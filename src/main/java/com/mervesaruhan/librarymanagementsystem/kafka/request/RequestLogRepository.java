package com.mervesaruhan.librarymanagementsystem.kafka.request;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "logTransactionManager")
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

}
