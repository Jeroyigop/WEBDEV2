package com.corales.webdev2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.corales.webdev2.exception.TaskNotFoundException;
import com.corales.webdev2.model.Task;
import com.corales.webdev2.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
