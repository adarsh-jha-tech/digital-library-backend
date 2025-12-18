package com.jhaadarsh.digital_library.adapter;

import com.jhaadarsh.digital_library.common.CommonAdapter;
import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.mappers.input.BookInputMapper;
import com.jhaadarsh.digital_library.model.BookModel;
import com.jhaadarsh.digital_library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
public class BookAdapter implements CommonAdapter<BookInputEntity, BookModel, Long> {

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
    @Override
    public BookModel create(BookInputEntity bookInputEntity) {
        return bookService.addBook(
                bookInputMapper.mapToModel(bookInputEntity)
        );
    }

    /**
     * Fetch all books.
     */
    @Override
    public List<BookModel> findAll() {
        return bookService.getAllBooks();
    }

    /**
     * Fetch a single book by ID.
     */
    @Override
    public BookModel findById(Long id) {
        return bookService.findById(id);
    }

    /**
     * Update an existing book (ID-driven).
     */
    @Override
    public BookModel update(Long id, BookInputEntity bookInput) {
        return bookService.updateBook(id, bookInput);
    }

    /**
     * Delete a book by ID.
     */
    @Override
    public void delete(Long id) {
        bookService.deleteBook(id);
    }
}
