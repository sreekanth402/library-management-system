package com.sreekanth.library.service;

import com.sreekanth.library.dto.BorrowRequest;
import com.sreekanth.library.dto.BorrowResponse;
import com.sreekanth.library.dto.DashboardStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BorrowService {
    BorrowResponse borrow(BorrowRequest request);
    BorrowResponse returnBook(Long borrowId);
    List<BorrowResponse> findByMember(Long memberId);
    List<BorrowResponse> findMine(String username);
    Page<BorrowResponse> findActive(Pageable pageable);
    Page<BorrowResponse> findHistory(Pageable pageable);
    DashboardStatsResponse stats();
}
