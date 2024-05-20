package momo.app.application.service;

import static momo.app.application.exception.ApplicationErrorCode.NOT_FOUND_APPLICATION;
import static momo.app.chat.exception.ChatErrorCode.CHAT_ROOM_NOT_FOUND;
import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import momo.app.application.domain.Application;
import momo.app.application.domain.ApplicationRepository;
import momo.app.application.dto.ApplicationCreateRequest;
import momo.app.auth.dto.AuthUser;
import momo.app.chat.domain.chatroom.ChatRoom;
import momo.app.chat.domain.chatroom.ChatRoomRepository;
import momo.app.chat.domain.chatroom.ChatRoomUser;
import momo.app.chat.domain.chatroom.ChatRoomUserRepository;
import momo.app.chat.exception.ChatErrorCode;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringMember;
import momo.app.gathering.domain.GatheringMemberRepository;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public Long createApplication(AuthUser authUser, ApplicationCreateRequest request) {
        User user = findUser(authUser.getId());
        Gathering gathering = findGathering(request.gatheringId());
        // TODO: 모임 정원 검증
        return applicationRepository.save(Application.builder()
                .user(user)
                .gathering(gathering)
                .build())
                .getId();
    }

    public void approveApplication(AuthUser authUser, Long id) {
        User user = findUser(authUser.getId());
        Application application = findApplicationWithGathering(id);
        Gathering gathering = application.getGathering();
        gathering.validateManager(authUser);
        application.approve();
        gatheringMemberRepository.save(GatheringMember.builder()
                .gathering(gathering)
                .user(user)
                .build());
        ChatRoom chatRoom = findChatRoom(gathering);
        chatRoomUserRepository.save(ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build());
    }

    private ChatRoom findChatRoom(Gathering gathering) {
        return chatRoomRepository.findById(gathering.getChatRoomId())
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));
    }

    private Application findApplicationWithGathering(Long id) {
        return applicationRepository.findByIdWithGathering(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_APPLICATION));
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
