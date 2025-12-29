package com.jhaadarsh.digital_library.adapter;

import com.jhaadarsh.digital_library.entity.input.UserInputEntity;
import com.jhaadarsh.digital_library.mappers.input.UserInputMapper;
import com.jhaadarsh.digital_library.model.UserModel;
import com.jhaadarsh.digital_library.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    private final UserInputMapper userInputMapper;
    private final UserService userService;

    public UserAdapter(UserInputMapper userInputMapper,
                       UserService userService) {
        this.userInputMapper = userInputMapper;
        this.userService = userService;
    }

    public UserModel create(UserInputEntity input) {
        return userService.create(userInputMapper.mapToModel(input));
    }

    public UserModel findById(Long id) {
        return userService.findById(id);
    }
}
