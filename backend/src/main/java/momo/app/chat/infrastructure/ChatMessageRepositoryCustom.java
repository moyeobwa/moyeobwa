package momo.app.chat.infrastructure;

import momo.app.chat.dto.response.ChatMessageResponse;
import momo.app.common.dto.SliceResponse;

public interface ChatMessageRepositoryCustom {

    SliceResponse<ChatMessageResponse> findByChatRoomIdOrderByIdDesc(int pageSize, String cursor, Long id);
}
