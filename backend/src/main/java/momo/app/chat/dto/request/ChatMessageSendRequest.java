package momo.app.chat.dto.request;

public record ChatMessageSendRequest(
        Long chatRoomId,
        String content,
        Long senderId
) {
}
