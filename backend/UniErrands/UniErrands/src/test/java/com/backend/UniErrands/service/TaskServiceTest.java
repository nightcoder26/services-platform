package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCategory(Task.Category.FOOD_DELIVERY);
        task.setReward(10.0);
        task.setUrgency(Task.Urgency.NOW);
        task.setLocation("Test Location");
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
    }

    @Test
    void getTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Optional<Task> foundTask = taskService.getTaskById(1L);
        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getTitle());
    }

    @Test
    void updateTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task updatedTask = taskService.updateTask(1L, task);
        assertNotNull(updatedTask);
        assertEquals("Test Task", updatedTask.getTitle());
    }

    @Test
    void deleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true); // Added setup for existsById
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
