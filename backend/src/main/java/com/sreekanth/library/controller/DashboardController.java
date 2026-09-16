package com.sreekanth.library.controller;

import com.sreekanth.library.dto.DashboardStatsResponse;
import com.sreekanth.library.service.BorrowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BorrowService borrowService;

    public DashboardController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','MEMBER')")
    public DashboardStatsResponse stats() {
        return borrowService.stats();
    }
}
