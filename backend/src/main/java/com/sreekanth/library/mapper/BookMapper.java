package com.sreekanth.library.mapper;

import com.sreekanth.library.dto.BookRequest;
import com.sreekanth.library.dto.BookResponse;
import com.sreekanth.library.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(request.getCategory())
                .publisher(request.getPublisher())
                .publicationYear(request.getPublicationYear())
                .totalQuantity(request.getTotalQuantity())
                .availableQuantity(request.getTotalQuantity())
                .build();
    }

    public void updateEntity(Book book, BookRequest request) {
        int borrowed = book.getTotalQuantity() - book.getAvailableQuantity();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setTotalQuantity(request.getTotalQuantity());
        book.setAvailableQuantity(Math.max(0, request.getTotalQuantity() - borrowed));
    }

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .publisher(book.getPublisher())
                .publicationYear(book.getPublicationYear())
                .totalQuantity(book.getTotalQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
