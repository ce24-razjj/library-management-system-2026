package com.pranavrajkota.library2026.controller;

import com.pranavrajkota.library2026.dto.BookDto;
import com.pranavrajkota.library2026.dto.PageResponse;
import com.pranavrajkota.library2026.dto.api_response;
import com.pranavrajkota.library2026.entity.BorrowRecord;
import com.pranavrajkota.library2026.service.BookServiceImpl;

import com.pranavrajkota.library2026.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookServiceImpl bookServiceimpl;
    private final BorrowService borrowService;

    public BookController(BookServiceImpl bookServiceimpl, BorrowService borrowService) {
        this.bookServiceimpl = bookServiceimpl;
        this.borrowService = borrowService;
    }

    // ---------------- GET ALL BOOKS ----------------

    @Operation(summary = "Get all books")
    @GetMapping
    public List<BookDto> getBooks() {
        return bookServiceimpl.getBookList();
    }

    // ---------------- ADD BOOK ----------------

    @Operation(summary = "Add a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public api_response<BookDto> addBook(@Valid @RequestBody BookDto dto) {

        BookDto book = bookServiceimpl.addBook(dto);

        return new api_response<>(
                "success",
                book,
                LocalDateTime.now()
        );
    }

    // ---------------- DELETE BOOK ----------------

    @Operation(summary = "Delete book by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        bookServiceimpl.deleteBook(id);
    }

    // ---------------- UPDATE BOOK ----------------

    @Operation(summary = "Update book completely")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable int id, @RequestBody BookDto dto) {
        return bookServiceimpl.updateBook(id, dto);
    }

    // ---------------- PARTIAL UPDATE ----------------

    @Operation(summary = "Partially update a book")
    @PatchMapping("/{id}")
    public BookDto tinyUpdate(@PathVariable int id, @RequestBody Map<String, Object> changes) {
        return bookServiceimpl.tinyUpdate(id, changes);
    }

    // ---------------- FIND BY AUTHOR ----------------

    @Operation(summary = "Get books by author")
    @GetMapping("/author/{author}")
    public List<BookDto> getBooksByAuthor(@PathVariable String author) {
        return bookServiceimpl.getBooksByAuthor(author);
    }

    // ---------------- FIND BY BOOK NAME ----------------

    @Operation(summary = "Get books by book name")
    @GetMapping("/name/{bookName}")
    public List<BookDto> getBookByName(@PathVariable String bookName) {
        return bookServiceimpl.getBookByName(bookName);
    }

    // ---------------- FILTER BOOKS ----------------

    @Operation(summary = "Search books by author / category / genre")
    @GetMapping("/search")
    public List<BookDto> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String genre
    ) {
        return bookServiceimpl.filterBooks(author, category, genre);
    }

    // ---------------- PAGINATION + SORTING ----------------
    @Operation(summary = "Get books with pagination and sorting")
    @GetMapping("/page")
    public PageResponse<BookDto> getBooksPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "bookName") String sortBy
    ) {
        return bookServiceimpl.getBooks(page, size, sortBy);
    }

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Integer bookId,
                             Principal principal) {

        String username = principal.getName();

        return borrowService.borrowBook(bookId, username);
    }

    @GetMapping("/my-books")
    public List<BorrowRecord> getMyBooks(Principal principal) {
        return borrowService.getMyBooks(principal.getName());
    }

    @GetMapping("/admin/borrowed")
    public List<BorrowRecord> getAllBorrowedBooks() {
        return borrowService.getAllBorrowedBooks();
    }

    @GetMapping("/admin/overdue")
    public List<BorrowRecord> getOverdueBooks() {
        return borrowService.getOverdueBooks();
    }

    @GetMapping("/admin/fines")
    public long getTotalFines() {
        return borrowService.getTotalFineCollected();
    }

}







































// // // // // // // // // // // // // // // // // / // // /// /// // /// // /// written of my own  ____________________________________________________________________

//package com.pranavrajkota.library2026.controller;
//
//import com.pranavrajkota.library2026.dto.BookDto;
//import com.pranavrajkota.library2026.dto.PageResponse;
//import com.pranavrajkota.library2026.dto.api_response;
//import com.pranavrajkota.library2026.entity.Book;
//import com.pranavrajkota.library2026.service.BookServiceImpl;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import jakarta.validation.Valid;
//import org.springframework.data.domain.Page;
//import org.springframework.web.bind.annotation.*;
//
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//import io.swagger.v3.oas.annotations.Operation;
//
//@RestController
//@RequestMapping("/books")
//public class BookController {
//
//    @GetMapping("/books")
//    public Page<Book> getBooks(
//            @RequestParam int page,
//            @RequestParam int size
//    ){
//        return bookServiceimpl.getBooks(page, size);  // getting me an error
//    } // this is getting me an error
//
//    @GetMapping("/search")
//    public List<BookDto> searchBooks(@RequestParam(required = false) String author, @RequestParam(required = false) String category, @RequestParam(required = false) String genre){
//        return bookServiceimpl.filterBooks(author, category, genre);
//    }
//
//    @Operation(summary = "Get all books")
//    @GetMapping
//    public List<BookDto> getBooks() {
//        return bookServiceimpl.getBookList();
//    }
//
//    @Operation(summary = "add a new book")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Book created successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid input")
//    })
//    @PostMapping
//    public api_response<BookDto> addBook(@Valid @RequestBody BookDto dto) {
//        BookDto book = bookServiceimpl.addBook(dto);
//
//        return new api_response<>(
//                "success",
//                book,
//                LocalDateTime.now()
//        );
//    }
//
//    @Operation(summary = "delete book by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
//            @ApiResponse(responseCode = "404", description = "Book not found")
//    })
//    @DeleteMapping("/{id}")
//    public void deleteBook(@PathVariable int id) {
//        bookServiceimpl.deleteBook(id);
//    }
//
//    @Operation(summary = "update a book by id and few info's")
//    @PutMapping("/{id}")
//    public BookDto updateBook(@PathVariable int id, @RequestBody BookDto dto) {
//        return bookServiceimpl.updateBook(id, dto);
//    }
//
//    @Operation(summary = "Get all books of (some) author")
//    @GetMapping("/author/{author}")
//    public List<BookDto> getBooksByAuthor(@PathVariable String author) {
//        return bookServiceimpl.getBooksByAuthor(author);
//    }
//
////    @PatchMapping("/{id}/{author}")
////    public boolean tinyUpdate(@PathVariable int id, @PathVariable String author) {
////        return bookServiceimpl.tinyUpdate(id, author);
////    }  // not scalable as if we have 20 different fields, then to update partially (patch) will we write 20 different methods ovsly not
//// so we have ---------------------->>>>>>>>>>>>>>>>>>>
//    @Operation(summary = "updating a book partially")
//    @PatchMapping("/{id}")
//    public BookDto tinyUpdate(@PathVariable int id, @RequestBody Map<String, Object> changes) {
//        return bookServiceimpl.tinyUpdate(id, changes);
//    }
//
//    @Operation(summary = "Get all books having book name common, for different authors")
//    @GetMapping("/name/{bookName}")
//    public List<BookDto> getBookByName(@PathVariable String bookName) {
//        return bookServiceimpl.getBookByName(bookName);
//    }
//
//    @Operation(summary = "get books page wise")
//    @GetMapping("/page")
//    public PageResponse<BookDto> getBooksPage(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy) {
//        return bookServiceimpl.getBooks(page, size, sortBy); // getting me an error
//    }
//
//    @GetMapping
//    public PageResponse<BookDto> getBooks(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam(defaultValue = "bookName") String sortBy
//    ) {
//        return bookServiceimpl.getBooks(page, size, sortBy);  // getting me an error
//    }
//
//
////
////     method 1 for dependency injection -> constructor injection // more preferred
//    private final BookServiceImpl bookServiceimpl;
//
//    public BookController(BookServiceImpl bookServiceimpl) {
//        this.bookServiceimpl = bookServiceimpl;
//    }
//
//
//    // method 2 for dependency injection -> field injection
////    @Autowired
////    private BookServiceImpl bookServiceimpl;
//
//
//    // method 3 for dependency injection -> setter injection
//
//}
