package com.sreekanth.library.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class FineCalculator {

    private FineCalculator() {
    }

    public static BigDecimal calculate(LocalDate dueDate, LocalDate returnDate, int finePerDay) {
        if (!returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }
        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return BigDecimal.valueOf(lateDays * (long) finePerDay);
    }
}
