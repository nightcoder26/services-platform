package com.backend.UniErrands.repository;

import com.backend.UniErrands.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
List<ChatMessage> findByTaskChat_IdOrderByTimestampAsc(Long taskChatId);

}
