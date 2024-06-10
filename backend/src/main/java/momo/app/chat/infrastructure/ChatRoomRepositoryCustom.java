package momo.app.chat.infrastructure;

public interface ChatRoomRepositoryCustom {

    boolean checkExistsBySenderIdAndChatRoomId(Long senderId, Long chatRoomId);
}
