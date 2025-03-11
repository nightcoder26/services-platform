package com.backend.UniErrands.service;

import com.backend.UniErrands.model.Task;
import com.backend.UniErrands.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        if (taskRepository.existsById(taskId)) {
            updatedTask.setId(taskId);
            return taskRepository.save(updatedTask);
        }
        return null; // or throw an exception
    }

    public void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        }
    }
}
