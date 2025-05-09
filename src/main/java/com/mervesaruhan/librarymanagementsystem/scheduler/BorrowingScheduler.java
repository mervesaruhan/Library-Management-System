package com.mervesaruhan.librarymanagementsystem.scheduler;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.service.BorrowingService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BorrowingScheduler {

    private final BorrowingService borrowingService;
    private final LogHelper logHelper;

    @Scheduled(cron = "0 0 2 * * ?")
    public void updateOverdueBorrowings() {
        logHelper.info("[SCHEDULER] Overdue borrowing scan started.");

        try {
            List<BorrowingDto> updatedList = borrowingService.getAndUpdateOverdueBorrowings();
            int count = updatedList.size();

            if (count > 0) {
                logHelper.info("[SCHEDULER] Overdue borrowing update completed. {} records marked as OVERDUE.", count);
            } else {
                logHelper.info("[SCHEDULER] No overdue borrowings found.");
            }

        } catch (Exception e) {
            logHelper.error("[SCHEDULER] Error during overdue borrowing check: {}", e.getMessage());
        }
    }
}
