package com.backend.UniErrands.controller;

import com.backend.UniErrands.model.ChatMessage;
import com.backend.UniErrands.service.ChatService;
import com.backend.UniErrands.Controller.ChatController; // Correct import statement
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessage() {
        ChatMessage message = new ChatMessage();
        message.setSenderId("user1");
        message.setReceiverId("user2");
        message.setMessage("Hello!");

        // Call the method to test
        ChatMessage response = chatController.sendMessage("taskId", message);

        // Verify that the saveMessage method was called
        verify(chatService).saveMessage(eq("taskId"), any(ChatMessage.class));

        assertNotNull(response);
        assertEquals("user1", response.getSenderId());
        assertEquals("user2", response.getReceiverId());
        assertEquals("Hello!", response.getMessage());
        assertNotNull(response.getTimestamp()); // Ensure timestamp is set
    }

    @Test
    public void testGetChatHistory() {
        when(chatService.getChatHistory("taskId")).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), chatController.getChatHistory("taskId"));
        verify(chatService).getChatHistory("taskId");
    }
}
