package com.jhaadarsh.digital_library.entity.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class UserInputEntity {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String phoneNumber;

    @NotNull(message = "Date of birth is mandatory")
    private Instant dob;
}
