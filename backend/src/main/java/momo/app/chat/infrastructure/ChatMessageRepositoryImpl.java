package momo.app.chat.infrastructure;

import static momo.app.chat.domain.chatmessage.QChatMessage.chatMessage;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.chat.dto.response.ChatMessageResponse;
import momo.app.common.dto.SliceResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SliceResponse<ChatMessageResponse> findByChatRoomIdOrderByDesc(
            int pageSize,
            String cursor,
            Long id
    ) {

        List<ChatMessageResponse> result = jpaQueryFactory.select(Projections.constructor(
                        ChatMessageResponse.class,
                        chatMessage.id,
                        chatMessage.content,
                        chatMessage.user.id,
                        chatMessage.user.name
                ))
                .from(chatMessage)
                .where(isInRange(cursor), chatRoomIdEq(id))
                .orderBy(chatMessage.id.desc())
                .limit(pageSize + 1)
                .fetch();

        return convertToSlice(result, pageSize);
    }

    private SliceResponse<ChatMessageResponse> convertToSlice(
            List<ChatMessageResponse>  chatMessageResponses,
            int pageSize
    ) {
        boolean hasNext = existsNextValue(chatMessageResponses, pageSize);
        String cursor = null;
        if (hasNext) {
            deleteLastValue(chatMessageResponses);
            cursor = createNextCursor(chatMessageResponses);
        }
        return SliceResponse.of(chatMessageResponses, hasNext, cursor);
    }

    private String createNextCursor(List<ChatMessageResponse> chatMessageResponses) {
        ChatMessageResponse chatMessageResponse = chatMessageResponses.get(chatMessageResponses.size() - 1);
        return String.valueOf(chatMessageResponse.id());
    }

    private void deleteLastValue(List<ChatMessageResponse> chatMessageResponses) {
        chatMessageResponses.remove(chatMessageResponses.size() - 1);
    }

    private boolean existsNextValue(List<ChatMessageResponse> chatMessageResponses, int pageSize) {
        if (chatMessageResponses.size() > pageSize) {
            return true;
        }
        return false;
    }

    private BooleanExpression chatRoomIdEq(Long id) {
        return chatMessage.chatRoom.id.eq(id);
    }

    private BooleanExpression isInRange(String cursor) {
        if (cursor == null) {
            return null;
        }

        return chatMessage.id.lt(Long.valueOf(cursor));
    }
}
