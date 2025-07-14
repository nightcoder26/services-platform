// --- Update 1: ChatController.java (WebSocket + REST controller) ---

package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.model.TaskChat;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.TaskChatRepository;
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository;
import com.backend.UniErrands.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private TaskChatRepository taskChatRepository;

  @MessageMapping("/sendMessage/{taskId}")
@SendTo("/topic/task/{taskId}")
public ChatMessage sendMessage(@DestinationVariable String taskId, ChatMessage message) {
    try {
        Long senderId;
        try {
            senderId = Long.parseLong(message.getSender());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid sender ID");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        Task task = taskRepository.findById(Long.parseLong(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // ✅ Only allow requester or accepted helper
        if (!(task.getRequester().getId().equals(senderId) ||
              (task.getHelper() != null && task.getHelper().getId().equals(senderId)))) {
            throw new IllegalArgumentException("❌ Access denied: You are not part of this task chat.");
        }

        message.setSender(sender.getUsername());
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

    } catch (Exception e) {
        // Gracefully return error message
        ChatMessage errorMessage = new ChatMessage();
        errorMessage.setSender("System");
        errorMessage.setContent(e.getMessage());
        errorMessage.setTimestamp(LocalDateTime.now());
        return errorMessage;
    }
}


 @GetMapping("/history/{taskId}")
public List<ChatMessage> getChatHistory(@PathVariable String taskId, @RequestParam Long userId) {
    Task task = taskRepository.findById(Long.parseLong(taskId))
            .orElseThrow(() -> new IllegalArgumentException("Task not found"));

    if (!(task.getRequester().getId().equals(userId) ||
          (task.getHelper() != null && task.getHelper().getId().equals(userId)))) {
        throw new IllegalArgumentException("Access denied: You are not allowed to view this chat.");
    }

    TaskChat chat = taskChatRepository.findByTaskId(taskId);
    return chat != null ? chatMessageService.getMessagesByTaskChatId(chat.getId()) : List.of();
}

}
