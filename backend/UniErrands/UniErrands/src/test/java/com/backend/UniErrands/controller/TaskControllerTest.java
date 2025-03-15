package com.backend.UniErrands.controller;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.service.TaskService;
import com.backend.UniErrands.Controller.TaskController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private Task task; // Define the task variable

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task(); // Initialize the task variable
    }

    @Test
    public void testBrowseTasks() {
        String category = "CLEANING";
        String urgency = "HIGH";
        Double min_price = 10.0;
        Double max_price = 100.0;
        Double distance = 5.0;

        List<Task> tasks = List.of(new Task());
        when(taskService.browseTasks(category, urgency, min_price, max_price, distance)).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.browseTasks(category, urgency, min_price, max_price, distance);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }

    @Test
    public void testRequestTaskAsRequester() {
        User user = new User();
        user.setRole("REQUESTER");
        Long taskId = 1L;

        ResponseEntity<String> response = taskController.requestTask(taskId, user);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only helpers or users with BOTH role can request tasks.", response.getBody());
    }

    @Test
    public void testRequestTaskAsHelper() {
        User user = new User();
        user.setRole("HELPER");
        Long taskId = 1L;

        doNothing().when(taskService).requestTask(taskId, user);

        ResponseEntity<String> response = taskController.requestTask(taskId, user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task requested successfully.", response.getBody());
    }

    @Test
    public void testUpdateTaskStatus() {
        Long taskId = 1L;
        String newStatus = "COMPLETED";
        task.setId(taskId);
        task.setStatus(Task.Status.PENDING);

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskService.updateTaskStatus(anyLong(), anyString())).thenAnswer(invocation -> {
            task.setStatus(Task.Status.valueOf(invocation.getArgument(1)));
            return task;
        });

        ResponseEntity<Task> response = taskController.updateTaskStatus(taskId, newStatus);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Task.Status.COMPLETED, response.getBody().getStatus());
    }

    @Test
    void testUpdateTaskStatusWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            task.updateStatus(null); // Check what status is being passed
        });
    }
    

    @Test
    public void testApproveHelperAsRequester() {
        Long taskId = 1L;
        Long helperId = 2L;
        User user = new User();
        user.setRole("REQUESTER");
        user.setId(3L);
        task.setRequester(user);

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        ResponseEntity<String> response = taskController.approveHelper(taskId, helperId, user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task has been approved and is now in progress.", response.getBody());
    }

    @Test
    public void testApproveHelperAsNonRequester() {
        Long taskId = 1L;
        Long helperId = 2L;
        User user = new User();
        user.setRole("REQUESTER");
        user.setId(3L);
        User taskCreator = new User();
        taskCreator.setId(4L);
        task.setRequester(taskCreator);

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        ResponseEntity<String> response = taskController.approveHelper(taskId, helperId, user);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only the task creator can approve a helper.", response.getBody());
    }

    @Test
    public void testApproveHelperWithInvalidRole() {
        Long taskId = 1L;
        Long helperId = 2L;
        User user = new User();
        user.setRole("HELPER");
        user.setId(3L);

        ResponseEntity<String> response = taskController.approveHelper(taskId, helperId, user);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only users with REQUESTER or BOTH role can approve a helper.", response.getBody());
    }

    @Test
    public void testGetMyTasks() {
        String role = "REQUESTER";
        List<Task> tasks = List.of(new Task());

        when(taskService.getMyTasks(role)).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getMyTasks(role);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }
}
