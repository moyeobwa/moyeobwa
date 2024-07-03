package momo.app.gathering.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.dto.SliceResponse;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.domain.GatheringSortType;
import momo.app.gathering.dto.GatheringResponse;
import momo.app.gathering.dto.GatheringNameResponse;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import static momo.app.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GatheringQueryService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    public SliceResponse<GatheringResponse> findAll(
            String cursor,
            int pageSize,
            GatheringSortType sortType,
            Category category
    ) {

        return gatheringRepository.findAllGatherings(cursor, pageSize, sortType, category);
    }

    public List<GatheringNameResponse> getUserGathering(AuthUser authUser) {
        User user = findUser(authUser.getId());
        return gatheringRepository.findAllGatheringsByUser(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

}
