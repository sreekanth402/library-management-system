package com.sreekanth.library.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiException {
    public BookNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Book not found: " + id);
    }
}
