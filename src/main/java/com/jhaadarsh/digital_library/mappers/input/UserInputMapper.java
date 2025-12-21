package com.jhaadarsh.digital_library.mappers.input;

import com.jhaadarsh.digital_library.entity.input.UserInputEntity;
import com.jhaadarsh.digital_library.model.UserModel;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserInputMapper {

    public UserModel mapToModel(UserInputEntity input) {
        return UserModel.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .phoneNumber(input.getPhoneNumber())
                .dob(input.getDob())
                .createdAt(Instant.now())
                .build();
    }
}
