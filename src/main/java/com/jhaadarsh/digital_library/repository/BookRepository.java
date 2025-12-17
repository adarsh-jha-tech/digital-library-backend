package com.jhaadarsh.digital_library.repository;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import com.jhaadarsh.digital_library.mappers.output.BookOutputMapper;
import com.jhaadarsh.digital_library.model.BookModel;
import com.jhaadarsh.digital_library.repository.jpa.BookJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * BookRepository
 *
 * Role:
 * -----
 * Acts as an abstraction over database access.
 *
 * Responsibilities:
 * - Convert between domain model and JPA entity
 * - Delegate actual DB operations to Spring Data JPA
 *
 * Why this exists:
 * - Keeps domain independent of JPA
 * - Easier to refactor / swap persistence later
 */
@Component
public class BookRepository {

    private final BookJPARepository bookJPARepository;
    private final BookOutputMapper bookOutputMapper;

    @Autowired
    public BookRepository(BookJPARepository bookJPARepository,
                          BookOutputMapper bookOutputMapper) {
        this.bookJPARepository = bookJPARepository;
        this.bookOutputMapper = bookOutputMapper;
    }

    /**
     * Save (create or update) a book.
     */
    public BookModel save(BookModel book) {
        BookOutputEntity entity =
                bookOutputMapper.mapFromModel(book);

        BookOutputEntity saved =
                bookJPARepository.save(entity);

        return bookOutputMapper.mapToModel(saved);
    }

    /**
     * Find a book by ID.
     */
    public Optional<BookModel> findById(long id) {
        return bookJPARepository.findById(id)
                .map(bookOutputMapper::mapToModel);
    }

    /**
     * Fetch all books.
     */
    public List<BookModel> findAll() {
        return bookOutputMapper
                .mapToModel(bookJPARepository.findAll());
    }

    /**
     * Delete a book.
     */
    public void deleteBook(BookModel book) {
        bookJPARepository.delete(
                bookOutputMapper.mapFromModel(book)
        );
    }
}
