package com.backend.UniErrands.controller; // Updated to match the new directory name

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.service.TaskService;
import com.backend.UniErrands.model.User; // Import User class

import com.backend.UniErrands.Controller.TaskController; // Added import for TaskController
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList; // Added import for ArrayList

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        User requester = new User(); // Create a new User object
        requester.setId(1L); // Set an ID for the requester
        task.setRequester(requester); // Set the requester in the Task

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        ResponseEntity<String> response = taskController.createTask(task);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task created successfully: " + task.getId(), response.getBody());

    }

    @Test
    void testGetTaskDetails() { // Updated method name
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(task));

        ResponseEntity<String> response = taskController.getTaskDetails(1L); // Updated method call
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskDetails(), response.getBody());
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        when(taskService.getAllTasks()).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getAllTasks();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(1L, task);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(anyLong());

        ResponseEntity<Void> response = taskController.deleteTask(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
