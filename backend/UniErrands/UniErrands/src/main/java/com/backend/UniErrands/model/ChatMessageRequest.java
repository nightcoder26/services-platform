package com.backend.UniErrands.model;

public class ChatMessageRequest {
    private String sender;
    private String content;

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
