package com.jhaadarsh.digital_library.repository;

import com.jhaadarsh.digital_library.entity.output.MembershipOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipJPARepository
        extends JpaRepository<MembershipOutputEntity, Long> {

    Optional<MembershipOutputEntity> findByUser_Id(Long userId);
}
