package com.jhaadarsh.digital_library.mappers.output;

import com.jhaadarsh.digital_library.entity.output.MembershipOutputEntity;
import com.jhaadarsh.digital_library.entity.output.UserOutputEntity;
import com.jhaadarsh.digital_library.model.MembershipModel;
import org.springframework.stereotype.Component;

@Component
public class MembershipOutputMapper {

    public MembershipOutputEntity mapFromModel(
            MembershipModel model,
            Long userId
    ) {
        MembershipOutputEntity entity = new MembershipOutputEntity();

        UserOutputEntity user = new UserOutputEntity();
        user.setId(userId);

        entity.setUser(user);
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setStatus(
                com.jhaadarsh.digital_library.enums.MembershipStatus
                        .valueOf(model.getStatus())
        );
        return entity;
    }

    public MembershipModel mapToModel(MembershipOutputEntity entity) {

        return MembershipModel.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
