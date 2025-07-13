package com.backend.UniErrands.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_chat_id")
    private TaskChat taskChat;
}
