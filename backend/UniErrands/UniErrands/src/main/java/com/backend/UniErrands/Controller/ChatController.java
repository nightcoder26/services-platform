package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import com.backend.UniErrands.model.User; // Import User model

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // WebSocket Endpoint: Send message to chat room
    @MessageMapping("/chat/{taskId}")
    @SendTo("/topic/messages/{taskId}")
    public ChatMessage sendMessage(@RequestBody ChatMessage message, @PathVariable String taskId) {
        // Additional validation for message content can be added here

        chatService.saveMessage(taskId, message);
        return message; // Message is broadcasted to all clients
    }

    // REST API: Get chat history from Redis
    @GetMapping("/history/{taskId}")
    public List<ChatMessage> getChatHistory(@PathVariable String taskId) {
        // Check if the task exists before returning chat history

        return chatService.getChatHistory(taskId);
    }
}
