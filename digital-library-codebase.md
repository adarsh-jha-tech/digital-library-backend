# Application Entry Point

## BookAdapter.java
```java
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

```

## LoggingAspect.java
```java
package com.jhaadarsh.digital_library.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // This advice runs before every method execution in the given package structure
    @Before("execution(* org.geeksforgeeks.gfg_spring_project..*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        log.info("Logging: Calling {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
}

```

## BookController.java
```java
package com.jhaadarsh.digital_library.controller;

import com.jhaadarsh.digital_library.adapter.BookAdapter;
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

    private final BookAdapter bookAdapter;

    @Autowired
    public BookController(BookAdapter bookAdapter) {
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
                .body(bookAdapter.save(book));
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

        BookModel updatedBook = bookAdapter.updateBook(id, book);
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
        bookAdapter.deleteBook(id);
        return ResponseEntity.ok("Deleted Book with id " + id + " successfully!");
    }
}

```

## DigitalLibraryApplication.java
```java
package com.jhaadarsh.digital_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalLibraryApplication.class, args);
	}

}

```

## BookInputEntity.java
```java
package com.jhaadarsh.digital_library.entity.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BookInputEntity {

    private long id;

    @NotBlank(message = "Mandatory Field!")
    private String name;
    @NotBlank(message = "Mandatory Field!")
    private String author;
    private String description;
    private Instant publishedDate;
}

```

## BookOutputEntity.java
```java
package com.jhaadarsh.digital_library.entity.output;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Data
@Table(name = "book")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOutputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "author", length = 100, nullable = false)
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "published_date")
    private Instant publishedDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

//    TODO : Revisit This (Genre & rating)
//    private String genre;
//    private int rating;





}

```

## ResourceNotFoundException.java
```java
package com.jhaadarsh.digital_library.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourceNotFoundException
 *
 * Role:
 * -----
 * Domain-level exception indicating a missing resource.
 *
 * Why custom exception:
 * - Meaningful error semantics
 * - Centralized logging
 * - Easy to map to HTTP later using @ControllerAdvice
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> cla,
                                     String fieldName,
                                     String fieldValue) {
        super("Resource of type " + cla.getSimpleName()
                + " with " + fieldName + " = " + fieldValue + " not found");

        log.error("Resource not found: {} {}={}",
                cla.getSimpleName(), fieldName, fieldValue);
    }
}

```

## BookInputMapper.java
```java
package com.jhaadarsh.digital_library.mappers.input;

import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.model.BookModel;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BookInputMapper {

    public BookModel mapToModel(BookInputEntity bookInputEntity){
        return BookModel.builder()
                .id(bookInputEntity.getId())
                .name(bookInputEntity.getName())
                .author(bookInputEntity.getAuthor())
                .description(bookInputEntity.getDescription())
                .publishedDate(bookInputEntity.getPublishedDate())
                .createdAt(Instant.now())
                .build();
    }

}

```

## BookOutputMapper.java
```java
package com.jhaadarsh.digital_library.mappers.output;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import com.jhaadarsh.digital_library.model.BookModel;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.util.*;

@Component
public class BookOutputMapper {

    public BookModel mapToModel(BookOutputEntity bookOutputEntity){
        return BookModel.builder()
                .id(bookOutputEntity.getId())
                .name(bookOutputEntity.getName())
                .author(bookOutputEntity.getAuthor())
                .description(bookOutputEntity.getDescription())
                .createdAt(bookOutputEntity.getCreatedAt())
                .updatedAt(bookOutputEntity.getUpdatedAt())
                .build();
    }

    public List<BookModel> mapToModel(List<BookOutputEntity> entities) {
        List<BookModel> models = new ArrayList<>();

        for (BookOutputEntity entity : entities) {
            models.add(BookModel.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .author(entity.getAuthor())
                    .description(entity.getDescription())
                    .publishedDate(entity.getPublishedDate())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build()
            );
        }

        return models;
    }


    public BookOutputEntity mapFromModel(BookModel bookModel){
        return BookOutputEntity.builder()
                .id(bookModel.getId())
                .name(bookModel.getName())
                .author(bookModel.getAuthor())
                .description(bookModel.getDescription())
                .publishedDate(bookModel.getPublishedDate())
                .createdAt(bookModel.getCreatedAt())
                .updatedAt((bookModel.getUpdatedAt()))
                .build();
    }
}
```

## BookModel.java
```java
package com.jhaadarsh.digital_library.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.Instant;

@Data
@Builder
@With
public class BookModel {

    private long id;
    private String name;
    private String author;
    private String description;
    private Instant publishedDate;
    private Instant createdAt;
    private Instant updatedAt;
}

```

## BookRepository.java
```java
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

```

## BookJPARepository.java
```java
package com.jhaadarsh.digital_library.repository.jpa;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookOutputEntity, Long> {
}

```

## BookService.java
```java
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

```

## ProjectCodeExporter.java
```java
package com.jhaadarsh.digital_library.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectCodeExporter {

    private static final String JAVA_ROOT =
            "src/main/java/com/jhaadarsh/digital_library";

    private static final String RESOURCES_ROOT =
            "src/main/resources";

    private static final String POM_FILE = "pom.xml";

    private static final String OUTPUT_FILE = "digital-library-codebase.md";

    // Layer name -> folder
    private static final Map<String, String> LAYERS = new LinkedHashMap<>();

    static {
        LAYERS.put("Application Entry Point", "");
        LAYERS.put("Controller Layer", "controller");
        LAYERS.put("Service Layer", "service");
        LAYERS.put("Adapter Layer", "adapter");
        LAYERS.put("Repository Layer", "repository");
        LAYERS.put("Domain Model", "model");
        LAYERS.put("Entities (Input / Output)", "entity");
        LAYERS.put("Mappers", "mappers");
        LAYERS.put("Exception Handling", "exception");
        LAYERS.put("Cross-Cutting Concerns", "beans");
    }

    public static void main(String[] args) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(OUTPUT_FILE),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {

            for (Map.Entry<String, String> layer : LAYERS.entrySet()) {
                writeLayer(writer, layer.getKey(), layer.getValue());
            }

            writeConfigFile(writer, POM_FILE, "Build Configuration (pom.xml)");
            writeConfigFile(writer,
                    RESOURCES_ROOT + "/application.properties",
                    "Application Configuration (application.properties)"
            );
        }

        System.out.println("✅ Export completed: " + OUTPUT_FILE);
    }

    private static void writeLayer(
            BufferedWriter writer,
            String heading,
            String folder
    ) throws IOException {

        Path basePath = folder.isEmpty()
                ? Paths.get(JAVA_ROOT)
                : Paths.get(JAVA_ROOT, folder);

        if (!Files.exists(basePath)) return;

        writer.write("# " + heading);
        writer.newLine();
        writer.newLine();

        Files.walk(basePath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> writeJavaFile(writer, p));
    }

    private static void writeJavaFile(
            BufferedWriter writer,
            Path file
    ) {
        try {
            writer.write("## " + file.getFileName());
            writer.newLine();
            writer.write("```java");
            writer.newLine();
            writer.write(Files.readString(file));
            writer.newLine();
            writer.write("```");
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeConfigFile(
            BufferedWriter writer,
            String filePath,
            String heading
    ) throws IOException {

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return;

        writer.write("# " + heading);
        writer.newLine();
        writer.newLine();
        writer.write("```");
        writer.newLine();
        writer.write(Files.readString(path));
        writer.newLine();
        writer.write("```");
        writer.newLine();
        writer.newLine();
    }
}

```

# Controller Layer

## BookController.java
```java
package com.jhaadarsh.digital_library.controller;

import com.jhaadarsh.digital_library.adapter.BookAdapter;
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

    private final BookAdapter bookAdapter;

    @Autowired
    public BookController(BookAdapter bookAdapter) {
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
                .body(bookAdapter.save(book));
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

        BookModel updatedBook = bookAdapter.updateBook(id, book);
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
        bookAdapter.deleteBook(id);
        return ResponseEntity.ok("Deleted Book with id " + id + " successfully!");
    }
}

```

# Service Layer

## BookService.java
```java
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

```

# Adapter Layer

## BookAdapter.java
```java
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

```

# Repository Layer

## BookRepository.java
```java
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

```

## BookJPARepository.java
```java
package com.jhaadarsh.digital_library.repository.jpa;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookOutputEntity, Long> {
}

```

# Domain Model

## BookModel.java
```java
package com.jhaadarsh.digital_library.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.Instant;

@Data
@Builder
@With
public class BookModel {

    private long id;
    private String name;
    private String author;
    private String description;
    private Instant publishedDate;
    private Instant createdAt;
    private Instant updatedAt;
}

```

# Entities (Input / Output)

## BookInputEntity.java
```java
package com.jhaadarsh.digital_library.entity.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BookInputEntity {

    private long id;

    @NotBlank(message = "Mandatory Field!")
    private String name;
    @NotBlank(message = "Mandatory Field!")
    private String author;
    private String description;
    private Instant publishedDate;
}

```

## BookOutputEntity.java
```java
package com.jhaadarsh.digital_library.entity.output;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Data
@Table(name = "book")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOutputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "author", length = 100, nullable = false)
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "published_date")
    private Instant publishedDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

//    TODO : Revisit This (Genre & rating)
//    private String genre;
//    private int rating;





}

```

# Mappers

## BookInputMapper.java
```java
package com.jhaadarsh.digital_library.mappers.input;

import com.jhaadarsh.digital_library.entity.input.BookInputEntity;
import com.jhaadarsh.digital_library.model.BookModel;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BookInputMapper {

    public BookModel mapToModel(BookInputEntity bookInputEntity){
        return BookModel.builder()
                .id(bookInputEntity.getId())
                .name(bookInputEntity.getName())
                .author(bookInputEntity.getAuthor())
                .description(bookInputEntity.getDescription())
                .publishedDate(bookInputEntity.getPublishedDate())
                .createdAt(Instant.now())
                .build();
    }

}

```

## BookOutputMapper.java
```java
package com.jhaadarsh.digital_library.mappers.output;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import com.jhaadarsh.digital_library.model.BookModel;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.util.*;

@Component
public class BookOutputMapper {

    public BookModel mapToModel(BookOutputEntity bookOutputEntity){
        return BookModel.builder()
                .id(bookOutputEntity.getId())
                .name(bookOutputEntity.getName())
                .author(bookOutputEntity.getAuthor())
                .description(bookOutputEntity.getDescription())
                .createdAt(bookOutputEntity.getCreatedAt())
                .updatedAt(bookOutputEntity.getUpdatedAt())
                .build();
    }

    public List<BookModel> mapToModel(List<BookOutputEntity> entities) {
        List<BookModel> models = new ArrayList<>();

        for (BookOutputEntity entity : entities) {
            models.add(BookModel.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .author(entity.getAuthor())
                    .description(entity.getDescription())
                    .publishedDate(entity.getPublishedDate())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build()
            );
        }

        return models;
    }


    public BookOutputEntity mapFromModel(BookModel bookModel){
        return BookOutputEntity.builder()
                .id(bookModel.getId())
                .name(bookModel.getName())
                .author(bookModel.getAuthor())
                .description(bookModel.getDescription())
                .publishedDate(bookModel.getPublishedDate())
                .createdAt(bookModel.getCreatedAt())
                .updatedAt((bookModel.getUpdatedAt()))
                .build();
    }
}
```

# Exception Handling

## ResourceNotFoundException.java
```java
package com.jhaadarsh.digital_library.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourceNotFoundException
 *
 * Role:
 * -----
 * Domain-level exception indicating a missing resource.
 *
 * Why custom exception:
 * - Meaningful error semantics
 * - Centralized logging
 * - Easy to map to HTTP later using @ControllerAdvice
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> cla,
                                     String fieldName,
                                     String fieldValue) {
        super("Resource of type " + cla.getSimpleName()
                + " with " + fieldName + " = " + fieldValue + " not found");

        log.error("Resource not found: {} {}={}",
                cla.getSimpleName(), fieldName, fieldValue);
    }
}

```

# Cross-Cutting Concerns

## LoggingAspect.java
```java
package com.jhaadarsh.digital_library.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // This advice runs before every method execution in the given package structure
    @Before("execution(* org.geeksforgeeks.gfg_spring_project..*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        log.info("Logging: Calling {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
}

```

# Build Configuration (pom.xml)

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.6</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.jhaadarsh</groupId>
	<artifactId>digital-library</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>Digital Library</name>
	<description>Digital Library Project Desc.</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>


<!--Aspect Dependency-->
        <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.7</version>
            <scope>runtime</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>1.9.7</version>
        </dependency>

	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```

# Application Configuration (application.properties)

```
spring.application.name=Digital Library

spring.datasource.url=jdbc:postgresql://localhost:5432/company_db
spring.datasource.username=postgres
spring.datasource.password=2001
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.default_schema=library_schema

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


server.error.include-stacktrace=never

```

