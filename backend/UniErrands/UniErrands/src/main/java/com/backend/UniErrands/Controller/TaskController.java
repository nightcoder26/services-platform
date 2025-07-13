package com.backend.UniErrands.Controller;

import com.backend.UniErrands.dto.TaskApproveHelperDTO;
import com.backend.UniErrands.dto.TaskRequestByHelperDTO;
import com.backend.UniErrands.dto.TaskRequestDTO;
import com.backend.UniErrands.dto.TaskApproveHelperDTO;
import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.service.TaskService;

import jakarta.validation.Valid;

import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PutMapping("/status/{taskId}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status) {
        try {
            Task updatedTask = taskService.updateTaskStatus(taskId, status);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Changed response type
        }
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

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskDetails(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/requester")
    public ResponseEntity<?> getRequesterId(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> {
                    if (task.getRequester() != null) {
                        return ResponseEntity.ok(task.getRequester().getId());
                    } else {
                        return ResponseEntity.badRequest().body("Requester not found for this task.");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

   @PostMapping("/request/{taskId}")
public ResponseEntity<String> requestTask(@PathVariable Long taskId, @RequestBody @Valid TaskRequestByHelperDTO dto) {
    Optional<User> optionalUser = userRepository.findById(dto.getId());
    if (optionalUser.isEmpty()) {
        return ResponseEntity.badRequest().body("User not found");
    }

    User user = optionalUser.get();
    if (!user.getRole().equals("HELPER") && !user.getRole().equals("BOTH")) {
        return ResponseEntity.status(403).body("Only helpers or users with BOTH role can request tasks.");
    }

    try {
        taskService.requestTask(taskId, user);
        return ResponseEntity.ok("Task request successful");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


    @PostMapping("/approve/{taskId}/{helperId}")
public ResponseEntity<String> approveHelper(
        @PathVariable Long taskId,
        @PathVariable Long helperId,
        @RequestBody @Valid TaskApproveHelperDTO dto) {

    if (!dto.getRole().equals("REQUESTER") && !dto.getRole().equals("BOTH")) {
        return ResponseEntity.status(403).body("Only users with REQUESTER or BOTH role can approve a helper.");
    }

    Optional<Task> optionalTask = taskService.getTaskById(taskId);
    if (optionalTask.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Task task = optionalTask.get();
    if (!task.getRequester().getId().equals(dto.getId())) {
        return ResponseEntity.status(403).body("Only the task creator can approve a helper.");
    }

    taskService.approveHelper(taskId, helperId);
    return ResponseEntity.ok("Task has been approved and is now in progress.");
}

    @PostMapping("/create")
public ResponseEntity<String> createTask(@RequestBody @Valid TaskRequestDTO dto) {
    Optional<User> requester = userRepository.findById(dto.getRequesterId());
    if (requester.isEmpty()) {
        return ResponseEntity.badRequest().body("Requester not found.");
    }

    Task task = new Task();
    task.setTitle(dto.getTitle());
    task.setDescription(dto.getDescription());
    try {
        task.setCategory(Task.Category.valueOf(dto.getCategory().toUpperCase()));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid category value.");
    }
    try {
        task.setUrgency(Task.Urgency.valueOf(dto.getUrgency().toUpperCase()));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid urgency value.");
    }
    task.setReward(dto.getPrice());
    task.setRequester(requester.get());
    task.setStatus(Task.Status.PENDING);
    System.out.println("DTO Title: " + dto.getTitle());
System.out.println("Requester ID: " + dto.getRequesterId());


    try {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok("Task created successfully: " + createdTask.getId());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
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
        if (role == null || (!role.equals("REQUESTER") && !role.equals("HELPER") && !role.equals("BOTH"))) {
            return ResponseEntity.badRequest().build(); // Fixed return type
        }
        List<Task> tasks = taskService.getMyTasks(role);
        return ResponseEntity.ok(tasks);
    }
}
