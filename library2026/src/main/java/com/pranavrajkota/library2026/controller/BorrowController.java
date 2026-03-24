package com.pranavrajkota.library2026.controller;

import com.pranavrajkota.library2026.service.BorrowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}")
    public String borrowBook(@PathVariable Integer bookId,
                             Authentication authentication) {

        String username = authentication.getName();

        return borrowService.borrowBook(bookId, username);
    }
}
