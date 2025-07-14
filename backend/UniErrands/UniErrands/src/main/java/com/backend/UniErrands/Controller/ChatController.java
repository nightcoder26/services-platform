// --- Update 1: ChatController.java (WebSocket + REST controller) ---

package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.model.TaskChat;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.TaskChatRepository;
import com.backend.UniErrands.repository.UserRepository;
import com.backend.UniErrands.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private TaskChatRepository taskChatRepository;

    @MessageMapping("/sendMessage/{taskId}")
    @SendTo("/topic/task/{taskId}")
    public ChatMessage sendMessage(@DestinationVariable String taskId, ChatMessage message) {
        Optional<User> userOpt = Optional.empty();
        try {
            Long senderId = Long.parseLong(message.getSender());
            userOpt = userRepository.findById(senderId);
        } catch (NumberFormatException e) {
            // Ignore; treat sender as username
        }

        userOpt.ifPresent(user -> message.setSender(user.getUsername()));
        message.setTimestamp(LocalDateTime.now());

        TaskChat taskChat = taskChatRepository.findByTaskId(taskId);
        if (taskChat == null) {
            taskChat = new TaskChat();
            taskChat.setTaskId(taskId);
            taskChat.setActive(true);
            taskChat = taskChatRepository.save(taskChat);
        }

        message.setTaskChat(taskChat);
        return chatMessageService.save(message);
    }

    @GetMapping("/history/{taskId}")
    public List<ChatMessage> getChatHistory(@PathVariable String taskId) {
        TaskChat chat = taskChatRepository.findByTaskId(taskId);
        if (chat == null) return List.of();
        return chatMessageService.getMessagesByTaskChatId(chat.getId());
    }
}
