package com.jhaadarsh.digital_library.repository.jpa;

import com.jhaadarsh.digital_library.entity.output.UserOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository
        extends JpaRepository<UserOutputEntity, Long> {
}
