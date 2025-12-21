package com.jhaadarsh.digital_library.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.Instant;

@Data
@Builder
@With
public class UserModel {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Instant dob;
    private Instant createdAt;
    private Instant updatedAt;
}
