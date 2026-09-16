package com.sreekanth.library.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String publisher;
    private Integer publicationYear;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Instant createdAt;
    private Instant updatedAt;
}
