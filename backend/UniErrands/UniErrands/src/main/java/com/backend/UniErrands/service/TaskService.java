package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Task createTask(Task task) {
        if (task.getRequester() != null) {
            User requester = task.getRequester();
            if (!requester.getRole().equals("HELPER") && !requester.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Requester must have the role 'HELPER' or 'BOTH' to request a task.");
            }
            if (userRepository.findById(requester.getId()).isEmpty()) {
                userService.createUser(requester);
            }
        }
        if (task.getHelper() != null) {
            User helper = task.getHelper();
            if (!helper.getRole().equals("HELPER") && !helper.getRole().equals("BOTH")) {
                throw new IllegalArgumentException("Helper must have the role 'HELPER' or 'BOTH'.");
            }
        }
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> getAllTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        if (taskRepository.existsById(taskId)) {
            updatedTask.setId(taskId);
            return taskRepository.save(updatedTask);
        }
        return null;
    }

    public void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        }
    }

    public List<Task> browseTasks(String category, String urgency, Double min_price, Double max_price, Double distance) {
        Task.Category taskCategory = null;
        Task.Urgency taskUrgency = null;
        if (category != null) {
            try {
                taskCategory = Task.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Collections.emptyList();
            }
        }
        if (urgency != null) {
            try {
                taskUrgency = Task.Urgency.valueOf(urgency.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Collections.emptyList();
            }
        }
        if (min_price != null && max_price != null && min_price > max_price) {
            return Collections.emptyList();
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
            if (task.getRequestedHelpers().contains(helper)) {
                throw new IllegalArgumentException("You have already requested this task.");
            }
            task.getRequestedHelpers().add(helper);
            task.setStatus(Task.Status.REQUESTED);
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
            if (!task.getRequestedHelpers().contains(helper)) {
                throw new IllegalArgumentException("Helper did not request this task.");
            }
            task.setHelper(helper);
            task.setStatus(Task.Status.APPROVED);
            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task not found.");
        }
    }

    public List<Task> getMyTasks(String role) {
        return taskRepository.findTasksByRole(role);
    }

    public Task updateTaskStatus(Long taskId, String newStatus) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new IllegalArgumentException("Task not found.");
        }
        Task task = optionalTask.get();
        Task.Status statusEnum;
        try {
            statusEnum = Task.Status.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Use IN_PROGRESS, COMPLETED, or CANCELLED.");
        }
        task.setStatus(statusEnum);
        return taskRepository.save(task);
    }
    
}
