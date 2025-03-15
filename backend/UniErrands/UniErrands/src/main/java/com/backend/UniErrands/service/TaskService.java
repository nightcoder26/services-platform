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
            if (!requester.getRole().equals("HELPER") && !requester.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Requester must have the role 'HELPER' or 'BOTH' to request a task.");
            }

            if (userRepository.findById(requester.getId()).isEmpty()) { 
                userService.createUser(requester); // Save the User if not already saved
            }
        }
        
        if (task.getHelper() != null) {
            User helper = task.getHelper(); // Use the helper field
            if (!helper.getRole().equals("HELPER") && !helper.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Helper must have the role 'HELPER' or 'BOTH'.");
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
    
        return taskRepository.findFilteredTasks(taskCategory, taskUrgency, min_price, max_price);

    }
    
    public void requestTask(Long taskId, User helper) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            
            if (!helper.getRole().equals("HELPER") && !helper.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Only HELPER or BOTH roles can request a task.");
            }
    
            // Ensure helper is not already in the list of requested helpers
            if (task.getRequestedHelpers().contains(helper)) {
                throw new IllegalArgumentException("You have already requested this task.");
            }
    
            task.getRequestedHelpers().add(helper); // Add helper to request list
            task.setStatus(Task.Status.REQUESTED); // Set the task status to REQUESTED

            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task not found.");
        }
    }
    

    public void approveHelper(Long taskId, Long helperId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            User helper = userRepository.findById(helperId).orElse(null);
    
            if (helper == null || (!helper.getRole().equals("HELPER") && !helper.getRole().equals("BOTH"))) {
                throw new IllegalArgumentException("Helper must have the role 'HELPER' or 'BOTH' to be approved.");
            }
    
            // Ensure helper actually requested the task
            if (!task.getRequestedHelpers().contains(helper)) {
                throw new IllegalArgumentException("Helper did not request this task.");
            }
    
            task.setHelper(helper); // Assign the helper to the task
            task.setStatus(Task.Status.APPROVED); // Update task status
            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task not found.");
        }
    }
    
    public List<Task> getMyTasks(String role) {
        return taskRepository.findTasksByRole(role); // Ensure this method is defined in TaskRepository
    }
}
