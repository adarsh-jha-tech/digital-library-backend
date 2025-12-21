package com.jhaadarsh.digital_library.service;

import com.jhaadarsh.digital_library.exception.ResourceNotFoundException;
import com.jhaadarsh.digital_library.model.UserModel;
import com.jhaadarsh.digital_library.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel create(UserModel user) {
        return userRepository.save(user);
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                UserModel.class,
                                "id",
                                id.toString()
                        )
                );
    }
}
