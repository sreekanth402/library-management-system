package com.sreekanth.library.exception;

import org.springframework.http.HttpStatus;

public class BookNotAvailableException extends ApiException {
    public BookNotAvailableException(String title) {
        super(HttpStatus.CONFLICT, "Book is not available: " + title);
    }
}
