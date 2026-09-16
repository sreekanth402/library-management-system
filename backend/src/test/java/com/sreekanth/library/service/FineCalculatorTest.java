package com.sreekanth.library.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FineCalculatorTest {

    @Test
    void noFineWhenReturnedOnTime() {
        LocalDate due = LocalDate.of(2026, 9, 20);
        assertEquals(BigDecimal.ZERO, FineCalculator.calculate(due, due, 10));
    }

    @Test
    void calculatesFineForLateDays() {
        LocalDate due = LocalDate.of(2026, 9, 20);
        LocalDate returned = LocalDate.of(2026, 9, 25);
        assertEquals(BigDecimal.valueOf(50), FineCalculator.calculate(due, returned, 10));
    }
}
