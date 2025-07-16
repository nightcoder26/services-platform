package com.backend.UniErrands.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendHelperApprovedNotification(String email, String taskTitle) {
      System.out.println("📨 Sending email to " + email + " for task: " + taskTitle);
        try {
            // Simulate email delay
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("✅ Email sent to: " + email);
    }
}
