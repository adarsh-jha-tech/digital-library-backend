package com.jhaadarsh.digital_library.controller;

import com.jhaadarsh.digital_library.adapter.MembershipAdapter;
import com.jhaadarsh.digital_library.entity.input.MembershipInputEntity;
import com.jhaadarsh.digital_library.model.MembershipModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    private final MembershipAdapter membershipAdapter;

    public MembershipController(MembershipAdapter membershipAdapter) {
        this.membershipAdapter = membershipAdapter;
    }

    @PostMapping
    public ResponseEntity<MembershipModel> createMembership(
            @Valid @RequestBody MembershipInputEntity input
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(membershipAdapter.create(input));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<MembershipModel> getMembershipByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                membershipAdapter.findByUserId(userId)
        );
    }

}
