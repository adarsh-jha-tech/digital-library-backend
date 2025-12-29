package com.jhaadarsh.digital_library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipModel {

    private Long id;

    private Long userId;

    private Instant startDate;

    private Instant endDate;

    private String status;

    private Instant createdAt;

    private Instant updatedAt;
}
