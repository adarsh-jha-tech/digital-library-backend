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
