package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/sendMessage/{taskId}") // changed to match client send destination
    @SendTo("/topic/task/{taskId}")   // /topic/task/{taskId}
    public ChatMessage sendMessage(@DestinationVariable String taskId, ChatMessage message) {
        // Map sender ID to username
        Optional<User> userOpt = Optional.empty();
        try {
            Long senderId = Long.parseLong(message.getSender());
            userOpt = userRepository.findById(senderId);
        } catch (NumberFormatException e) {
            // sender is not a valid ID, keep as is
        }
        if (userOpt.isPresent()) {
            message.setSender(userOpt.get().getUsername());
        }
        return message;
    }
}
