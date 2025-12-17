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