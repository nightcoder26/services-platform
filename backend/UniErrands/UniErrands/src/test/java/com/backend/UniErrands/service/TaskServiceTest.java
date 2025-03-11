package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User; // Ensure User is imported
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository; // Ensure UserRepository is imported

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository; // Mock UserRepository

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBrowseTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setCategory(Task.Category.MISCELLANEOUS); // Use the correct enum type
        
        Task task2 = new Task();
        task2.setId(2L);
        task2.setCategory(Task.Category.MISCELLANEOUS); // Use the correct enum type

        when(taskRepository.findFilteredTasks(any(), any(), any(), any(), any())).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.browseTasks(null, null,null, null, null);
        assertEquals(2, tasks.size());
    }

    @Test
    public void testRequestTask() {
        Long taskId = 1L;
        User requester = new User();
        requester.setRole("REQUESTER"); // Set the role for the requester
        taskService.requestTask(taskId, requester); // Pass the User object
        verify(taskRepository, times(1)).findById(taskId);
    }


    @Test
    public void testApproveHelper() {
        Long taskId = 1L;
        Long helperId = 2L;

        Task task = new Task();
        task.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        User helper = new User();
        helper.setRole("HELPER"); // Set the role for the helper

        when(userRepository.findById(helperId)).thenReturn(Optional.of(helper));

        taskService.approveHelper(taskId, helperId);

        assertEquals(Task.Status.APPROVED, task.getStatus()); // Check if status is set to APPROVED
        assertEquals(helper, task.getHelper()); // Check if the helper is set correctly
        verify(taskRepository, times(1)).save(task); // Ensure save is called
    }

    @Test
    public void testGetMyTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        User requester = new User(); // Use the default constructor or adjust as necessary
        requester.setUsername("requester");
        requester.setEmail("requester@example.com");
        requester.setRole("REQUESTER");
        task1.setRequester(requester);

        when(taskRepository.findTasksByRole("requester")).thenReturn(Arrays.asList(task1));

        List<Task> tasks = taskService.getMyTasks("requester");
        assertEquals(1, tasks.size());
    }
}
