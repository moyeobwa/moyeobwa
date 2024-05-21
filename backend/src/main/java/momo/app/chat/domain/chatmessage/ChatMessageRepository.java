package momo.app.chat.domain.chatmessage;

import momo.app.chat.infrastructure.ChatMessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
}
