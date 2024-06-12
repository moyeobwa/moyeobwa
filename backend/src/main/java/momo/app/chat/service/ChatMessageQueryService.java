package momo.app.chat.service;

import static momo.app.chat.exception.ChatErrorCode.USER_NOT_IN_CHAT_ROOM;
import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.chat.domain.chatmessage.ChatMessageRepository;
import momo.app.chat.domain.chatroom.ChatRoomRepository;
import momo.app.chat.dto.response.ChatMessageResponse;
import momo.app.common.dto.SliceResponse;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final GatheringRepository gatheringRepository;

    public SliceResponse<ChatMessageResponse> findAllChatMessages(
            Long id,
            AuthUser authUser,
            String cursor,
            int pageSize
    ) {
        Gathering gathering = findGathering(id);
        validateUserInChatRoom(gathering.getChatRoomId(), authUser);
        return chatMessageRepository.findByChatRoomIdOrderByIdDesc(pageSize, cursor, id);
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }


    private void validateUserInChatRoom(Long id, AuthUser authUser) {
        if (!chatRoomRepository.checkExistsBySenderIdAndChatRoomId(authUser.getId(), id)) {
            throw new BusinessException(USER_NOT_IN_CHAT_ROOM);
        }
    }
}
