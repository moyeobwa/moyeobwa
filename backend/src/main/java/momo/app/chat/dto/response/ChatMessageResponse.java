package momo.app.chat.dto.response;

public record ChatMessageResponse(
        Long id,
        String content,
        Long senderId,
        String senderName
) {

    public static ChatMessageResponse of(Long id, String content, Long memberId, String senderName) {
        return new ChatMessageResponse(id, content, memberId, senderName);
    }
}
