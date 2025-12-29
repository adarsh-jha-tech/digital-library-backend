package com.jhaadarsh.digital_library.entity.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class MembershipInputEntity {

    @NotNull(message = "User id is mandatory")
    private Long userId;

    @NotNull(message = "Membership start date is mandatory")
    private Instant startDate;

    @NotNull(message = "Membership end date is mandatory")
    private Instant endDate;
}
