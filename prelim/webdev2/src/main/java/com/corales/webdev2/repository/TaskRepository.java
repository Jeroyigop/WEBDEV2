package com.corales.webdev2.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.corales.webdev2.model.Task;

@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    private Long nextId = 1L;

    public TaskRepository() {
        tasks.add(new Task(
                nextId++,
                "Finish WEBDEV2 Exam",
                "Complete the Task Tracker application.",
                false
        ));

        tasks.add(new Task(
                nextId++,
                "Record Video",
                "Record the walkthrough of the application.",
                false
        ));
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    public Task save(Task task) {
        task.setId(nextId++);
        tasks.add(task);
        return task;
    }
}