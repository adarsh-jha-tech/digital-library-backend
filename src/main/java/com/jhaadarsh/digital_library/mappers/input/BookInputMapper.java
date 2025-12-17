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
