package com.sreekanth.library.controller;

import com.sreekanth.library.dto.BorrowResponse;
import com.sreekanth.library.service.BorrowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/return")
public class ReturnController {

    private final BorrowService borrowService;

    public ReturnController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{borrowId}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public BorrowResponse returnBook(@PathVariable Long borrowId) {
        return borrowService.returnBook(borrowId);
    }
}
