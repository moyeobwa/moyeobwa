package momo.app.chat.controller;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.chat.dto.request.ChatMessageSendRequest;
import momo.app.chat.dto.response.ChatMessageResponse;
import momo.app.chat.service.ChatMessageCommandService;
import momo.app.chat.service.ChatMessageQueryService;
import momo.app.common.dto.SliceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageCommandService chatMessageCommandService;
    private final ChatMessageQueryService chatMessageQueryService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessageSendRequest request) {
        chatMessageCommandService.save(request);
        sendingOperations.convertAndSend("/topic/chat-rooms/" + request.chatRoomId(), request);
    }

    @GetMapping("/api/v1/chat-messages/chat-rooms/{id}")
    public ResponseEntity<SliceResponse<ChatMessageResponse>> findMessages(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int pageSize,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(chatMessageQueryService.findAllChatMessages(id, authUser, cursor, pageSize));
    }

}
