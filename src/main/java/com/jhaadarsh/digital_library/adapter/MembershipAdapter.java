package com.jhaadarsh.digital_library.adapter;

import com.jhaadarsh.digital_library.entity.input.MembershipInputEntity;
import com.jhaadarsh.digital_library.entity.output.MembershipOutputEntity;
import com.jhaadarsh.digital_library.enums.MembershipStatus;
import com.jhaadarsh.digital_library.mappers.input.MembershipInputMapper;
import com.jhaadarsh.digital_library.mappers.output.MembershipOutputMapper;
import com.jhaadarsh.digital_library.model.MembershipModel;
import com.jhaadarsh.digital_library.repository.MembershipJPARepository;
import org.springframework.stereotype.Component;

@Component
public class MembershipAdapter {

    private final MembershipJPARepository membershipRepository;
    private final UserAdapter userAdapter;
    private final MembershipInputMapper inputMapper;
    private final MembershipOutputMapper outputMapper;

    public MembershipAdapter(
            MembershipJPARepository membershipRepository,
            UserAdapter userAdapter,
            MembershipInputMapper inputMapper,
            MembershipOutputMapper outputMapper
    ) {
        this.membershipRepository = membershipRepository;
        this.userAdapter = userAdapter;
        this.inputMapper = inputMapper;
        this.outputMapper = outputMapper;
    }

    public MembershipModel create(MembershipInputEntity input) {

        // 1️⃣ Verify user exists (throws if not)
        var user = userAdapter.findById(input.getUserId());

        // 2️⃣ Prevent duplicate membership
        membershipRepository.findByUser_Id(user.getId())
                .ifPresent(m -> {
                    throw new IllegalStateException(
                            "Membership already exists for user id " + user.getId()
                    );
                });

        // 3️⃣ Input → Model
        MembershipModel model = inputMapper.mapToModel(input);

        // 4️⃣ Set business state
        model.setStatus(MembershipStatus.Active.name());

        // 5️⃣ Model → OutputEntity
        MembershipOutputEntity entity =
                outputMapper.mapFromModel(model, user.getId());

        // 6️⃣ Save
        MembershipOutputEntity saved = membershipRepository.save(entity);

        // 7️⃣ OutputEntity → Model (return to controller)
        return outputMapper.mapToModel(saved);
    }

    public MembershipModel findByUserId(Long userId) {

        MembershipOutputEntity entity = membershipRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No membership found for user id " + userId
                        )
                );

        return outputMapper.mapToModel(entity);
    }

}
