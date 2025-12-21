package com.jhaadarsh.digital_library.entity.output;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Table(name = "UserEntity")
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private Instant dob;


}
