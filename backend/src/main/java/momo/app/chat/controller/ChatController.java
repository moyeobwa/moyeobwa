package momo.app.chat.controller;

import lombok.RequiredArgsConstructor;
import momo.app.chat.dto.request.ChatMessageSendRequest;
import momo.app.chat.service.ChatMessageCommandService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageCommandService chatMessageCommandService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessageSendRequest request) {
        chatMessageCommandService.save(request);
        sendingOperations.convertAndSend("/topic/chat-rooms/" + request.chatRoomId(), request);
    }
}
