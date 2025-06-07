package com.backend.UniErrands.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private LocalDateTime timestamp;

    // Constructor
    public ChatMessage(String senderId, String receiverId, String message, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // No-argument constructor
    public ChatMessage() {}

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
