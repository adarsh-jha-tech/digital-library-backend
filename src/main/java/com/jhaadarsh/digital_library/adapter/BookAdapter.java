package com.jhaadarsh.digital_library.adapter;

import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import com.jhaadarsh.digital_library.mappers.input.BookInputMapper;
import com.jhaadarsh.digital_library.mappers.output.BookOutputMapper;
import com.jhaadarsh.digital_library.model.BookModel;
import com.jhaadarsh.digital_library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * BookAdapter
 *
 * Role:
 * -----
 * Acts as an Application / Use-Case layer.
 *
 * Responsibilities:
 * - Orchestrate flow between Controller and Service
 * - Convert request DTOs into domain models (via mappers)
 * - Keep controllers thin
 *
 * Why this exists:
 * - Prevents controllers from becoming fat
 * - Central place to coordinate application logic
 */
@Component
public class BookAdapter {

    private final BookInputMapper bookInputMapper;
    private final BookService bookService;

    @Autowired
    public BookAdapter(BookInputMapper bookInputMapper, BookService bookService) {
        this.bookInputMapper = bookInputMapper;
        this.bookService = bookService;
    }

    /**
     * Create a new book.
     *
     * Flow:
     * InputEntity → Domain Model → Service → Repository
     */
    public BookModel save(BookInputEntity bookInputEntity) {
        return bookService.addBook(
                bookInputMapper.mapToModel(bookInputEntity)
        );
    }

    /**
     * Fetch all books.
     */
    public List<BookModel> findAll() {
        return bookService.getAllBooks();
    }

    /**
     * Fetch a single book by ID.
     */
    public BookModel findById(Long id) {
        return bookService.findById(id);
    }

    /**
     * Update an existing book (ID-driven).
     */
    public BookModel updateBook(Long id, BookInputEntity bookInput) {
        return bookService.updateBook(id, bookInput);
    }

    /**
     * Delete a book by ID.
     */
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }
}
