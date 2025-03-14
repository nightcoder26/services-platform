package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.TaskRepository;
import com.backend.UniErrands.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTaskWithBothRole() {
        User requester = new User();
        requester.setRole("BOTH");
        requester.setId(1L);

        User helper = new User();
        helper.setRole("HELPER");
        helper.setId(2L);

        Task task = new Task();
        task.setRequester(requester);
        task.setHelper(helper);

        when(userRepository.findById(requester.getId())).thenReturn(Optional.of(requester));
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals(requester.getId(), createdTask.getRequester().getId());
    }

    @Test
    public void testRequestTaskWithBothRole() {
        User requester = new User();
        requester.setRole("BOTH");
        requester.setId(1L);

        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.requestTask(task.getId(), requester);
        assertEquals(Task.Status.REQUESTED, task.getStatus());
        verify(taskRepository).save(task);
    }

    // Additional tests for other scenarios can be added here
}
