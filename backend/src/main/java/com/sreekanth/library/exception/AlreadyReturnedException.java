package com.sreekanth.library.exception;

import org.springframework.http.HttpStatus;

public class AlreadyReturnedException extends ApiException {
    public AlreadyReturnedException(Long borrowId) {
        super(HttpStatus.CONFLICT, "Borrow record already returned: " + borrowId);
    }
}
