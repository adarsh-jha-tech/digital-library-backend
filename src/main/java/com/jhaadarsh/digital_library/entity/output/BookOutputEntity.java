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
