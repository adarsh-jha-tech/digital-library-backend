package com.jhaadarsh.digital_library.common;

import java.util.List;

public interface CommonAdapter<I, O, ID> {

    O create(I input);

    List<O> findAll();

    O findById(ID id);

    O update(ID id, I input);

    void delete(ID id);
}
