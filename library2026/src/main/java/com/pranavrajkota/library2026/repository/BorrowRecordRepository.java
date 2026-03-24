package com.pranavrajkota.library2026.repository;

import com.pranavrajkota.library2026.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;


public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    Optional<BorrowRecord> findByBookIdAndUserUserNameAndReturnDateIsNull(Integer bookId, String username);
    List<BorrowRecord> findByUserUserNameAndReturnDateIsNull(String userName);
    List<BorrowRecord> findAllByReturnDateIsNull();
    List<BorrowRecord> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
}
