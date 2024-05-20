package momo.app.chat.infrastructure;

public interface ChatRoomRepositoryCustom {

    boolean existsBySenderIdAndChatRoomId(Long userId, Long chatRoomId);
}
