package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        // Fetch the existing task from the database
        Optional<Task> existingTaskOptional = taskService.findById(taskId);
        
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            
            // Prevent updating the email and password of the requester
            if (task.getRequester() != null) {
                task.getRequester().setEmail(existingTask.getRequester().getEmail());  // Retain the old email
                task.getRequester().setPassword(existingTask.getRequester().getPassword());  // Retain the old password
            }
            
            // Now update the task with the new data (excluding email and password)
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCategory(task.getCategory());
            existingTask.setReward(task.getReward());
            existingTask.setUrgency(task.getUrgency());
            existingTask.setLocation(task.getLocation());
            existingTask.setIsPrivate(task.getIsPrivate());
            
            // Save the updated task
            Task updatedTask = taskService.save(existingTask);
            
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
