package com.sreekanth.library.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ApiException {
    public MemberNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Member not found: " + id);
    }
}
