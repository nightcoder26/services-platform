package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User; // Import User class
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository; // Import UserRepository
import com.backend.UniErrands.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository; // Declare UserRepository

    @Autowired
    private UserService userService; // Declare UserService

    public Task createTask(Task task) {
        // Ensure the associated User is saved before creating the Task
        if (task.getRequester() != null) {
            User requester = task.getRequester(); // Use the requester field
            if (!requester.getRole().equals("REQUESTER") && !requester.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Requester must have the role 'REQUESTER' or 'BOTH'.");
            }
            if (userRepository.findById(requester.getId()).isEmpty()) { 
                userService.createUser(requester); // Save the User if not already saved
            }
        }
        
        if (task.getHelper() != null) {
            User helper = task.getHelper(); // Use the helper field
            if (!helper.getRole().equals("HELPER") && !helper.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Helper must have the role 'HELPER'.");
            }
        } else {
            throw new IllegalArgumentException("Task must have a valid helper.");
        }
        
        return taskRepository.save(task); // Save the Task after ensuring User is saved
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        if (taskRepository.existsById(taskId)) {
            updatedTask.setId(taskId);
            return taskRepository.save(updatedTask);
        }
        return null; // or throw an exception
    }

    public void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        }
    }

    // New methods for task browsing and acceptance
    public List<Task> browseTasks(String category, String urgency, Double min_price, Double max_price, Double distance) {
        Task.Category taskCategory = null;
        Task.Urgency taskUrgency = null;
    
        // Convert category to Enum safely
        if (category != null) {
            try {
                taskCategory = Task.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Collections.emptyList(); // Invalid category -> return empty list
            }
        }
    
        // Convert urgency to Enum safely
        if (urgency != null) {
            try {
                taskUrgency = Task.Urgency.valueOf(urgency.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Collections.emptyList(); // Invalid urgency -> return empty list
            }
        }
    
        // Handle invalid price range cases
        if (min_price != null && max_price != null && min_price > max_price) {
            return Collections.emptyList(); // Invalid price range (min > max) -> return empty list
        }
    
        return taskRepository.findFilteredTasks(taskCategory, taskUrgency, min_price, max_price, distance);
    }
    
    public void requestTask(Long taskId, User requester) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (!requester.getRole().equals("REQUESTER") && !requester.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Requester must have the role 'REQUESTER' or 'BOTH' to request a task.");
            }
            task.setStatus(Task.Status.REQUESTED); // Assuming there's a status field
            taskRepository.save(task); // Save the updated task
        } else {
            // Handle the case where the task is not found
        }
    }

    public void approveHelper(Long taskId, Long helperId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            User helper = userRepository.findById(helperId).orElse(null); // Fetch the User object
            if (helper == null || !helper.getRole().equals("HELPER")) {
                throw new IllegalArgumentException("Helper must have the role 'HELPER' to be approved.");
            }
            task.setApprovedHelperId(helper); // Set the helper directly

            task.setStatus(Task.Status.APPROVED); // Assuming there's a status field
            taskRepository.save(task); // Save the updated task
        } else {
            // Handle the case where the task is not found
        }
    }

    public List<Task> getMyTasks(String role) {
        return taskRepository.findTasksByRole(role); // Ensure this method is defined in TaskRepository
    }
}
