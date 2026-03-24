package com.pranavrajkota.library2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    Optional<BorrowedBook> findByBookIdAndUserUserNameAndReturnDateIsNull(Integer bookId, String username);
}
