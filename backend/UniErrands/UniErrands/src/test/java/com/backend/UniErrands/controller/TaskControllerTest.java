package com.backend.UniErrands.controller;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private com.backend.UniErrands.Controller.TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void testRequestTaskAsBoth() {
        User user = new User();
        user.setRole("BOTH");
        Long taskId = 1L;

        doNothing().when(taskService).requestTask(taskId, user);

        ResponseEntity<String> response = taskController.requestTask(taskId, user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task requested successfully.", response.getBody());
    }

    @Test
    public void testRequestTaskWithValidHelper() {
        User user = new User();
        user.setRole("HELPER");
        Long taskId = 17L;

        doNothing().when(taskService).requestTask(taskId, user);

        ResponseEntity<String> response = taskController.requestTask(taskId, user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task requested successfully.", response.getBody());
    }

    @Test
    public void testApproveHelperAsRequester() {
        Long taskId = 1L;
        Long helperId = 2L;
        User user = new User();
        user.setRole("REQUESTER");
        user.setId(3L);
        
        Task task = new Task();
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
        
        Task task = new Task();
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
