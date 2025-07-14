package com.backend.UniErrands.service;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesByTaskChatId(Long taskChatId) {
        return chatMessageRepository.findByTaskChat_IdOrderByTimestampAsc(taskChatId);
    }
}
