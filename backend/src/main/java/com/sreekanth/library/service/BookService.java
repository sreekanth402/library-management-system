package com.sreekanth.library.service;

import com.sreekanth.library.dto.BookRequest;
import com.sreekanth.library.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookResponse> search(String title, String author, String category, Pageable pageable);
    BookResponse getById(Long id);
    BookResponse create(BookRequest request);
    BookResponse update(Long id, BookRequest request);
    void delete(Long id);
}
