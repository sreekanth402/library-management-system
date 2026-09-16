package com.sreekanth.library.mapper;

import com.sreekanth.library.dto.BorrowResponse;
import com.sreekanth.library.entity.BorrowRecord;
import com.sreekanth.library.entity.BorrowStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BorrowMapper {

    public BorrowResponse toResponse(BorrowRecord record) {
        boolean overdue = record.getStatus() == BorrowStatus.BORROWED
                && record.getDueDate().isBefore(LocalDate.now());
        return BorrowResponse.builder()
                .id(record.getId())
                .bookId(record.getBook().getId())
                .bookTitle(record.getBook().getTitle())
                .memberId(record.getMember().getId())
                .memberName(record.getMember().getName())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus())
                .overdue(overdue)
                .fine(record.getFine())
                .build();
    }
}
