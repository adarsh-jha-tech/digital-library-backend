package com.jhaadarsh.digital_library.mappers.output;

import com.jhaadarsh.digital_library.entity.output.UserOutputEntity;
import com.jhaadarsh.digital_library.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserOutputMapper {

    public UserModel mapToModel(UserOutputEntity entity) {
        return UserModel.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .dob(entity.getDob())
                .build();
    }

    public UserOutputEntity mapFromModel(UserModel model) {
        UserOutputEntity entity = new UserOutputEntity();
        entity.setId(model.getId());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setEmail(model.getEmail());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setDob(model.getDob());
        return entity;
    }
}
