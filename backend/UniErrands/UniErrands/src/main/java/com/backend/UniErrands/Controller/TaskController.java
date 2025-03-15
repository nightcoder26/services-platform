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
    public ResponseEntity<Task> getTaskDetails(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/requester")
    public ResponseEntity<Long> getRequesterId(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(task.getRequester().getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/requester/{id}/{subId}")
    public ResponseEntity<?> handleRequesterRequest(@PathVariable Long id, @PathVariable Long subId) {
        // Implement logic to handle the request for the specific task and requester
        // This is a placeholder for the actual implementation
        return ResponseEntity.ok("Handled request for task ID: " + id + " and sub ID: " + subId);
    }

    @GetMapping("/browse")
    public ResponseEntity<List<Task>> browseTasks(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String urgency,
        @RequestParam(required = false) Double min_price,
        @RequestParam(required = false) Double max_price,
        @RequestParam(required = false) Double distance) {
    
        List<Task> tasks = taskService.browseTasks(category, urgency, min_price, max_price, distance);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/request/{taskId}")
    public ResponseEntity<String> requestTask(@PathVariable Long taskId, @RequestBody User user) {
        if (!user.getRole().equals("HELPER") && !user.getRole().equals("BOTH")) {
            return ResponseEntity.status(403).body("Only helpers or users with BOTH role can request tasks.");
        }

        taskService.requestTask(taskId, user);
        return ResponseEntity.ok("Task requested successfully.");
    }

    @PostMapping("/approve/{taskId}/{helperId}")
    public ResponseEntity<String> approveHelper(
            @PathVariable Long taskId,
            @PathVariable Long helperId,
            @RequestBody User user) {

        // Ensure only 'REQUESTER' or 'BOTH' roles can approve
        if (!user.getRole().equals("REQUESTER") && !user.getRole().equals("BOTH")) {
            return ResponseEntity.status(403).body("Only users with REQUESTER or BOTH role can approve a helper.");
        }

        // Ensure only the task creator can approve
        Task task = taskService.getTaskById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        if (!task.getRequester().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Only the task creator can approve a helper.");
        }

        taskService.approveHelper(taskId, helperId);
        return ResponseEntity.ok("Task has been approved and is now in progress.");
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
