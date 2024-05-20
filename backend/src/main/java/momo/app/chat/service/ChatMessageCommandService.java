package momo.app.chat.service;

import static momo.app.chat.exception.ChatErrorCode.CHAT_ROOM_NOT_FOUND;
import static momo.app.chat.exception.ChatErrorCode.USER_NOT_IN_CHAT_ROOM;

import lombok.RequiredArgsConstructor;
import momo.app.chat.domain.chatmessage.ChatMessage;
import momo.app.chat.domain.chatmessage.ChatMessageRepository;
import momo.app.chat.domain.chatroom.ChatRoom;
import momo.app.chat.domain.chatroom.ChatRoomRepository;
import momo.app.chat.dto.request.ChatMessageSendRequest;
import momo.app.common.error.exception.BusinessException;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void save(ChatMessageSendRequest request) {
        ChatRoom chatRoom = findChatRoom(request.chatRoomId());
        validateMemberInChatRoom(request.senderId(), request.chatRoomId());
        User sender = findUser(request.senderId());
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .user(sender)
                .content(request.content())
                .build());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    private ChatRoom findChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));
    }

    private void validateMemberInChatRoom(Long senderId, Long chatRoomId) {
        if (!chatRoomRepository.existsBySenderIdAndChatRoomId(senderId, chatRoomId)) {
            throw new BusinessException(USER_NOT_IN_CHAT_ROOM);
        }
    }
}
