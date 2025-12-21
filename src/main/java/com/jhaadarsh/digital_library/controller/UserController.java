package com.jhaadarsh.digital_library.controller;

import com.jhaadarsh.digital_library.adapter.UserAdapter;
import com.jhaadarsh.digital_library.entity.input.UserInputEntity;
import com.jhaadarsh.digital_library.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserAdapter userAdapter;

    public UserController(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @PostMapping
    public ResponseEntity<UserModel> createUser(
            @Valid @RequestBody UserInputEntity input) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAdapter.create(input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userAdapter.findById(id));
    }
}
