package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // ✅ WebSocket Endpoint: Send message to chat room
    @MessageMapping("/chat/{taskId}")  // Clients send messages here
    @SendTo("/topic/messages/{taskId}")  // Messages are broadcasted here
    public ChatMessage sendMessage(@PathVariable String taskId, ChatMessage message) {
        // Set timestamp for message
        message.setTimestamp(LocalDateTime.now());

        // Save message in Redis
        chatService.saveMessage(taskId, message);

        return message; // Message is broadcasted to all subscribers
    }

    // ✅ REST API: Get chat history from Redis
    @GetMapping("/history/{taskId}")
    public List<ChatMessage> getChatHistory(@PathVariable String taskId) {
        return chatService.getChatHistory(taskId);
    }
}
