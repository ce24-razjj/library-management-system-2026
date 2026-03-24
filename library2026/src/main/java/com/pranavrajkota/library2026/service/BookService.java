package com.pranavrajkota.library2026.service;

import com.pranavrajkota.library2026.dto.BookDto;
import com.pranavrajkota.library2026.dto.PageResponse;
import com.pranavrajkota.library2026.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BookService {

    List<BookDto> getBookList();

    BookDto addBook(BookDto dto);

    void deleteBook(int id);

    BookDto updateBook(int id, BookDto dto);

    List<BookDto> getBooksByAuthor(String author);

    BookDto tinyUpdate(int id, Map<String,Object> changes);

    List<BookDto> getBookByName(String bookName);

    PageResponse<BookDto> getBooks(int page, int size, String sortBy);
}
