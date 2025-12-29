package com.jhaadarsh.digital_library.repository;

import com.jhaadarsh.digital_library.entity.output.UserOutputEntity;
import com.jhaadarsh.digital_library.mappers.output.UserOutputMapper;
import com.jhaadarsh.digital_library.model.UserModel;
import com.jhaadarsh.digital_library.repository.jpa.UserJPARepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepository {

    private final UserJPARepository userJPARepository;
    private final UserOutputMapper userOutputMapper;

    public UserRepository(UserJPARepository userJPARepository,
                          UserOutputMapper userOutputMapper) {
        this.userJPARepository = userJPARepository;
        this.userOutputMapper = userOutputMapper;
    }

    public UserModel save(UserModel model) {
        UserOutputEntity saved =
                userJPARepository.save(
                        userOutputMapper.mapFromModel(model)
                );
        return userOutputMapper.mapToModel(saved);
    }

    public Optional<UserModel> findById(Long id) {
        return userJPARepository.findById(id)
                .map(entity -> userOutputMapper.mapToModel(entity));
    }
}
