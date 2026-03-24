package com.pranavrajkota.library2026.repository;

import com.pranavrajkota.library2026.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // for dynamic queries

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    List<Book> findByAuthor_NameIgnoreCase(String author);
    List<Book> findByBookNameIgnoreCase(String bookName);
    Page<Book> findAll(Pageable pageable);
}
