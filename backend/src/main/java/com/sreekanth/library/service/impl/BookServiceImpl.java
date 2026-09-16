package com.sreekanth.library.service.impl;

import com.sreekanth.library.dto.BookRequest;
import com.sreekanth.library.dto.BookResponse;
import com.sreekanth.library.entity.Book;
import com.sreekanth.library.exception.ApiException;
import com.sreekanth.library.exception.BookNotFoundException;
import com.sreekanth.library.mapper.BookMapper;
import com.sreekanth.library.repository.BookRepository;
import com.sreekanth.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> search(String title, String author, String category, Pageable pageable) {
        return bookRepository.search(blankToNull(title), blankToNull(author), blankToNull(category), pageable)
                .map(bookMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return bookMapper.toResponse(find(id));
    }

    @Override
    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ApiException(HttpStatus.CONFLICT, "ISBN already exists: " + request.getIsbn());
        }
        return bookMapper.toResponse(bookRepository.save(bookMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = find(id);
        if (bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new ApiException(HttpStatus.CONFLICT, "ISBN already exists: " + request.getIsbn());
        }
        bookMapper.updateEntity(book, request);
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = find(id);
        bookRepository.delete(book);
    }

    private Book find(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private String blankToNull(String value) {
        return StringUtils.hasText(value) ? value : null;
    }
}
