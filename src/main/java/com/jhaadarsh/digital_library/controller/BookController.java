package com.jhaadarsh.digital_library.controller;

import com.jhaadarsh.digital_library.common.CommonAdapter;
import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookController
 *
 * Role:
 * -----
 * Acts as the HTTP entry point (Inbound Adapter) for Book-related operations.
 *
 * Responsibilities:
 * - Expose REST endpoints
 * - Handle HTTP semantics (URL, status codes, request/response)
 * - Delegate business work to Adapter layer
 *
 * What it DOES NOT do:
 * - No business logic
 * - No database interaction
 * - No entity/model manipulation
 */
@RestController
@RequestMapping("book")
public class BookController {

    private final CommonAdapter<BookInputEntity, BookModel, Long> bookAdapter;


    @Autowired
    public BookController(
            CommonAdapter<BookInputEntity, BookModel, Long> bookAdapter) {
        this.bookAdapter = bookAdapter;
    }


    /**
     * Fetch all books.
     *
     * HTTP: GET /book
     * Response: 200 OK + List of books
     */
    @GetMapping
    public ResponseEntity<List<BookModel>> getAllBooks() {
        return ResponseEntity.ok(bookAdapter.findAll());
    }

    /**
     * Fetch a single book by ID.
     *
     * HTTP: GET /book/{id}
     * Response:
     * - 200 OK → book found
     * - 404 NOT FOUND → book does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookModel> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookAdapter.findById(id));
    }

    /**
     * Create a new book.
     *
     * HTTP: POST /book/add
     * Response:
     * - 201 CREATED → book successfully created
     */
    @PostMapping("/add")
    public ResponseEntity<BookModel> addBook(@RequestBody BookInputEntity book) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookAdapter.create(book));
    }

    /**
     * Update an existing book.
     *
     * HTTP: PUT /book/{id}
     *
     * Important:
     * - ID comes from URL (source of truth)
     * - Body contains fields to update
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookModel> updateBook(
            @PathVariable Long id,
            @RequestBody BookInputEntity book) {

        BookModel updatedBook = bookAdapter.update(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Delete a book by ID.
     *
     * HTTP: DELETE /book/{id}
     * Response:
     * - 200 OK → deleted successfully
     * - 404 NOT FOUND → book does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookAdapter.delete(id);
        return ResponseEntity.ok("Deleted Book with id " + id + " successfully!");
    }
}
