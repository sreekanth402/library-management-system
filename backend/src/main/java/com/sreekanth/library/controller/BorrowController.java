package com.sreekanth.library.controller;

import com.sreekanth.library.dto.BorrowRequest;
import com.sreekanth.library.dto.BorrowResponse;
import com.sreekanth.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public BorrowResponse borrow(@Valid @RequestBody BorrowRequest request) {
        return borrowService.borrow(request);
    }

    @PostMapping("/return/{borrowId}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public BorrowResponse returnBook(@PathVariable Long borrowId) {
        return borrowService.returnBook(borrowId);
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public List<BorrowResponse> byMember(@PathVariable Long memberId) {
        return borrowService.findByMember(memberId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public List<BorrowResponse> mine(Authentication authentication) {
        return borrowService.findMine(authentication.getName());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public Page<BorrowResponse> active(@PageableDefault(size = 10) Pageable pageable) {
        return borrowService.findActive(pageable);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public Page<BorrowResponse> history(@PageableDefault(size = 10) Pageable pageable) {
        return borrowService.findHistory(pageable);
    }
}
