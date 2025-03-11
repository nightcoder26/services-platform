package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User; // Import User class
import com.backend.UniErrands.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTaskDetails(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(task.getTaskDetails()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/browse")
    public ResponseEntity<List<Task>> browseTasks(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String urgency,
        @RequestParam(required = false) Double min_price, // New min price
        @RequestParam(required = false) Double max_price,
        @RequestParam(required = false) Double distance) {
    
        // Log the category parameter for debugging
        System.out.println("Category parameter received: " + category);
        
        Task.Category taskCategory = null;
        if (category != null) {
            try {
                taskCategory = Task.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Handle invalid category
            }
        }

        List<Task> tasks = taskService.browseTasks(category, urgency,  min_price, max_price, distance);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/request/{taskId}")
    public ResponseEntity<Void> requestTask(@PathVariable Long taskId, @RequestBody User user) {
        taskService.requestTask(taskId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve/{taskId}/{helperId}")
    public ResponseEntity<Void> approveHelper(@PathVariable Long taskId, @PathVariable Long helperId) {
        taskService.approveHelper(taskId, helperId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        // Ensure the requester is set before creating the task
        if (task.getRequester() == null) {
            return ResponseEntity.badRequest().body("Requester must be set."); // Return bad request if requester is not set
        }
        try {
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.ok("Task created successfully: " + createdTask.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Return error message if role check fails
        }


    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Task task = taskService.updateTask(taskId, updatedTask);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<Task>> getMyTasks(@RequestParam String role) {
        List<Task> tasks = taskService.getMyTasks(role);
        return ResponseEntity.ok(tasks);
    }
}
