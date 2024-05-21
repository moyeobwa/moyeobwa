package momo.app.chat.service;

import static momo.app.chat.exception.ChatErrorCode.USER_NOT_IN_CHAT_ROOM;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.chat.domain.chatmessage.ChatMessageRepository;
import momo.app.chat.domain.chatroom.ChatRoomRepository;
import momo.app.chat.dto.response.ChatMessageResponse;
import momo.app.common.dto.SliceResponse;
import momo.app.common.error.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SliceResponse<ChatMessageResponse> findAllChatMessages(
            Long id,
            AuthUser authUser,
            String cursor,
            int pageSize
    ) {
        validateUserInChatRoom(id, authUser);
        return chatMessageRepository.findByChatRoomIdOrderByDesc(pageSize, cursor, id);
    }


    private void validateUserInChatRoom(Long id, AuthUser authUser) {
        if (!chatRoomRepository.existsBySenderIdAndChatRoomId(authUser.getId(), id)) {
            throw new BusinessException(USER_NOT_IN_CHAT_ROOM);
        }
    }
}
