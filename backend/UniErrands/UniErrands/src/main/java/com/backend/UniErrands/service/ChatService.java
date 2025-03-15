package com.backend.UniErrands.service;

import com.backend.UniErrands.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {

    @Autowired
    private RedisTemplate<String, ChatMessage> redisTemplate;

    private static final long CHAT_EXPIRATION = 24; // Store messages for 24 hours

    public void saveMessage(String taskId, ChatMessage message) {
        ListOperations<String, ChatMessage> listOps = redisTemplate.opsForList();
        listOps.rightPush("chat:" + taskId, message);
        redisTemplate.expire("chat:" + taskId, CHAT_EXPIRATION, TimeUnit.HOURS);
    }

    public List<ChatMessage> getChatHistory(String taskId) {
        ListOperations<String, ChatMessage> listOps = redisTemplate.opsForList();
        return listOps.range("chat:" + taskId, 0, -1);
    }
}
