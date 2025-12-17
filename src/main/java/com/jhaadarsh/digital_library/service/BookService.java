package com.jhaadarsh.digital_library.service;

import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import com.jhaadarsh.digital_library.exception.ResourceNotFoundException;
import com.jhaadarsh.digital_library.model.BookModel;
import com.jhaadarsh.digital_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * BookService
 *
 * Role:
 * -----
 * Contains core business logic for Book domain.
 *
 * Responsibilities:
 * - Enforce business rules
 * - Decide when to throw domain exceptions
 * - Control update semantics (load → mutate → save)
 *
 * What it does NOT know:
 * - HTTP
 * - Controllers
 * - JSON
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Create a new book.
     */
    public BookModel addBook(BookModel book) {
        return bookRepository.save(book);
    }

    /**
     * Find a book by ID.
     *
     * Throws:
     * - ResourceNotFoundException if book does not exist
     */
    public BookModel findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                BookModel.class,
                                "id",
                                String.valueOf(id)
                        )
                );
    }

    /**
     * Fetch all books.
     */
    public List<BookModel> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Update an existing book.
     *
     * Standard enterprise update pattern:
     * 1. Load existing entity
     * 2. Mutate allowed fields
     * 3. Save same entity
     */
    public BookModel updateBook(Long id, BookInputEntity bookInput) {

        BookModel existingBook = findById(id);

        existingBook.setName(bookInput.getName());
        existingBook.setAuthor(bookInput.getAuthor());
        existingBook.setDescription(bookInput.getDescription());
        existingBook.setPublishedDate(bookInput.getPublishedDate());
        existingBook.setUpdatedAt(Instant.now());

        return bookRepository.save(existingBook);
    }

    /**
     * Delete a book by ID.
     */
    public void deleteBook(Long id) {
        BookModel book = findById(id);
        bookRepository.deleteBook(book);
    }
}
