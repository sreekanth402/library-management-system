package com.sreekanth.library.service.impl;

import com.sreekanth.library.dto.BookRequest;
import com.sreekanth.library.entity.Book;
import com.sreekanth.library.exception.ApiException;
import com.sreekanth.library.mapper.BookMapper;
import com.sreekanth.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, new BookMapper());
    }

    @Test
    void createRejectsDuplicateIsbn() {
        BookRequest request = new BookRequest();
        request.setTitle("Effective Java");
        request.setAuthor("Joshua Bloch");
        request.setIsbn("9780134685991");
        request.setCategory("Programming");
        request.setTotalQuantity(5);
        when(bookRepository.existsByIsbn("9780134685991")).thenReturn(true);

        assertThrows(ApiException.class, () -> bookService.create(request));
    }

    @Test
    void deleteRemovesExistingBook() {
        Book book = Book.builder().id(1L).title("Clean Code").isbn("9780132350884").build();
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        bookService.delete(1L);

        verify(bookRepository).delete(book);
    }
}
