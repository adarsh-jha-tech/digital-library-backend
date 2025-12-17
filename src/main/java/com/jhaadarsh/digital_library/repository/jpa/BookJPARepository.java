package com.jhaadarsh.digital_library.repository.jpa;

import com.jhaadarsh.digital_library.entity.output.BookOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookOutputEntity, Long> {
}
