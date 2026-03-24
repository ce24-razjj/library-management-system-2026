package com.pranavrajkota.library2026.service;

import com.pranavrajkota.library2026.entity.Book;
import com.pranavrajkota.library2026.entity.BorrowRecord;
import com.pranavrajkota.library2026.entity.User;
import com.pranavrajkota.library2026.repository.BookRepository;
import com.pranavrajkota.library2026.repository.BorrowRecordRepository;
import com.pranavrajkota.library2026.repository.BorrowedBookRepository;
import com.pranavrajkota.library2026.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(BookRepository bookRepository,
                         UserRepository userRepository,
                         BorrowedBookRepository borrowedBookRepository,
                         BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowedBookRepository = borrowedBookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public String borrowBook(Integer bookId, String username) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book not available");
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 prevent duplicate borrow
        borrowRecordRepository
                .findByBookIdAndUserUserNameAndReturnDateIsNull(bookId, username)
                .ifPresent(r -> {
                    throw new RuntimeException("You already borrowed this book");
                });

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(7)); // 7 days limit
        record.setReturned(false);

        borrowRecordRepository.save(record);

        return "Book borrowed successfully";
    }

    public String returnBook(Integer bookId, String username) {

        BorrowRecord record = borrowRecordRepository
                .findByBookIdAndUserUserNameAndReturnDateIsNull(bookId, username)
                .orElseThrow(() -> new RuntimeException("No active borrow found"));

        Book book = record.getBook();

        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        record.setReturnDate(LocalDate.now());
        record.setReturned(true);
        borrowRecordRepository.save(record);

        long fine = calculateFine(record);

        return "Book returned. Fine = Rs " + fine;
    }

    public List<BorrowRecord> getMyBooks(String username) {
        return borrowRecordRepository
                .findByUserUserNameAndReturnDateIsNull(username);
    }

    public long calculateFine(BorrowRecord record){

        if (record.getReturnDate() == null) return 0;

        long daysLate = ChronoUnit.DAYS.between(
                record.getDueDate(),
                record.getReturnDate()
        );

        return daysLate > 0 ? daysLate * 10 : 0; // rs10 per day
    }

    public List<BorrowRecord> getAllBorrowedBooks() {
        return borrowRecordRepository.findAllByReturnDateIsNull();
    }

    public List<BorrowRecord> getOverdueBooks() {
        return borrowRecordRepository
                .findByReturnDateIsNullAndDueDateBefore(LocalDate.now());
    }

    public long getTotalFineCollected() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        long total = 0
        for (BorrowRecord r : records) {
            if (r.getReturnDate() != null) {
                total += calculateFine(r);
            }
        }
        return total;
    }
}