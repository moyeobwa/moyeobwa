package momo.app.gathering.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.dto.SliceResponse;
import momo.app.gathering.domain.*;
import momo.app.gathering.dto.GatheringResponse;
import momo.app.gathering.dto.GatheringUserResponse;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GatheringQueryService {

    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserRepository userRepository;

    public SliceResponse<GatheringResponse> findAll(
            String cursor,
            int pageSize,
            GatheringSortType sortType,
            Category category
    ) {

        return gatheringRepository.findAllGatherings(cursor, pageSize, sortType, category);
    }

    public List<GatheringUserResponse> getUserGathering(AuthUser authUser) {
        User user = findUser(authUser.getId());
        return gatheringRepository.findAllGatheringsByUser(user);
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("gathering not found"));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

}
