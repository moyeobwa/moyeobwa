package momo.app.chat.infrastructure;

import static momo.app.chat.domain.chatroom.QChatRoom.chatRoom;
import static momo.app.chat.domain.chatroom.QChatRoomUser.chatRoomUser;
import static momo.app.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import momo.app.chat.domain.chatroom.ChatRoom;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean checkExistsBySenderIdAndChatRoomId(Long userId, Long chatRoomId) {
        ChatRoom result = jpaQueryFactory.selectFrom(chatRoom)
                .join(chatRoom.chatRoomUsers, chatRoomUser)
                .join(chatRoomUser.user, user)
                .where(chatRoomIdEq(chatRoomId)
                                .and(isChatRoomManager(userId)
                                        .or(isInChatRoom(userId))
                                )
                )
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression chatRoomIdEq(Long chatRoomId) {
        return chatRoom.id.eq(chatRoomId);
    }

    private BooleanExpression isChatRoomManager(Long userId) {
        return chatRoom.managerId.eq(userId);
    }

    private BooleanExpression isInChatRoom(Long userId) {
        return user.id.eq(userId);
    }
}
