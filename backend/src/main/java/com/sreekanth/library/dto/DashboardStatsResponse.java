package com.sreekanth.library.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DashboardStatsResponse {
    private long totalBooks;
    private long totalMembers;
    private long activeBorrows;
    private BigDecimal totalFines;
}
