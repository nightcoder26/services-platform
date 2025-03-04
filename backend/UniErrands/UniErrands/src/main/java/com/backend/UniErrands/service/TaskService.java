package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.backend.UniErrands.model.User;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setCategory(updatedTask.getCategory());
            existingTask.setReward(updatedTask.getReward());
            existingTask.setUrgency(updatedTask.getUrgency());
            existingTask.setLocation(updatedTask.getLocation());
            existingTask.setIsPrivate(updatedTask.getIsPrivate());
            return taskRepository.save(existingTask);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> browseTasks(String category, String urgency, Double priceRange, Long userId) {
        return taskRepository.findFilteredTasks(category, urgency, priceRange, userId);
    }

    public void requestTask(Long taskId, Long helperId) {
        // Logic to handle task request by a helper
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setHelper(new User(helperId)); // Assuming User is already fetched
            taskRepository.save(task);
        }
    }

    public void approveHelper(Long taskId, Long helperId) {
        // Logic to approve a helper for a task
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setHelper(new User(helperId)); // Assuming User is already fetched
            task.setStatus(Task.Status.ACCEPTED);
            taskRepository.save(task);
        }
    }

    public List<Task> getMyTasks(Long userId, String role) {
        // Logic to fetch tasks based on user role
        return taskRepository.findTasksByRole(userId, role);
    }
}

