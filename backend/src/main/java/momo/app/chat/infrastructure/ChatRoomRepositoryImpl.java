package momo.app.chat.infrastructure;

import static momo.app.chat.domain.chatroom.QChatRoom.chatRoom;
import static momo.app.chat.domain.chatroom.QChatRoomUser.chatRoomUser;
import static momo.app.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import momo.app.chat.domain.chatroom.ChatRoom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsBySenderIdAndChatRoomId(Long userId, Long chatRoomId) {
        ChatRoom result = jpaQueryFactory.selectFrom(chatRoom)
                .join(chatRoom.chatRoomUsers, chatRoomUser)
                .join(chatRoomUser.user, user)
                .where(chatRoom.id.eq(chatRoomId), chatRoom.managerId.eq(userId).or(user.id.eq(userId)))
                .fetchFirst();

        return result != null;
    }
}
