package com.jhaadarsh.digital_library.mappers.input;

import com.jhaadarsh.digital_library.entity.input.MembershipInputEntity;
import com.jhaadarsh.digital_library.model.MembershipModel;
import org.springframework.stereotype.Component;

@Component
public class MembershipInputMapper {

    public MembershipModel mapToModel(MembershipInputEntity input) {

        return MembershipModel.builder()
                .userId(input.getUserId())
                .startDate(input.getStartDate())
                .endDate(input.getEndDate())
                .build();
    }
}
