package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.TaskChat;
import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.repository.TaskChatRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-chat")
@CrossOrigin("http://localhost:5173")
public class TaskChatController {

    @Autowired
    private TaskChatRepository taskChatRepository;

    @GetMapping("/messages")
    public List<ChatMessage> getMessages(@RequestParam String taskId) {
        TaskChat chat = taskChatRepository.findByTaskId(taskId);
        if (chat != null && chat.isActive()) {
            return chat.getMessages();
        }
        return List.of(); // Return empty if not active or not found
    }

    @PutMapping("/deactivate")
    public Map<String, String> deactivateChat(@RequestParam String taskId) {
        TaskChat chat = taskChatRepository.findByTaskId(taskId);
        if (chat != null) {
            chat.setActive(false);
            taskChatRepository.save(chat);
            return Map.of("message", "Chat deactivated for task: " + taskId);
        }
        return Map.of("error", "No chat found");
    }
}
