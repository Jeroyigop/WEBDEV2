package com.corales.webdev2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    private Long nextId = 1L;

    public TaskRepository() {
        tasks.add(new Task(nextId++, "Finish WebDev Lab", "Complete the Week 5 CRUD laboratory activity.", 1, "In Progress"));
        tasks.add(new Task(nextId++, "Study Java", "Review Java classes and Spring Boot concepts.", 2, "Pending"));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public Task findById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    public Task save(Task task) {
        task.setId(nextId++);
        tasks.add(task);
        return task;
    }

    public Task update(Long id, Task updatedTask) {
        Task existingTask = findById(id);

        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setStatus(updatedTask.getStatus());
        }

        return existingTask;
    }

    public boolean delete(Long id) {
        Task task = findById(id);

        if (task != null) {
            tasks.remove(task);
            return true;
        }

        return false;
    }
}