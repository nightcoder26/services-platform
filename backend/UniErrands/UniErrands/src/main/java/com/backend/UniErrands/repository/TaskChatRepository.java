package com.backend.UniErrands.repository;

import com.backend.UniErrands.model.TaskChat;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.*;

public interface TaskChatRepository extends JpaRepository<TaskChat,Long> {
    TaskChat findByTaskId(String taskId);
    void deleteByTaskId(String taskId);
}
