package com.corales.webdev2;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Task {

    private Long id;

    @NotBlank(message = "Task title is required.")
    @Size(min = 3, max = 100, message = "Task title must be between 3 and 100 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    private String description;

    @Min(value = 1, message = "Priority must be at least 1.")
    @Max(value = 5, message = "Priority must not exceed 5.")
    private int priority;

    @NotBlank(message = "Status is required.")
    private String status;

    public Task() {
    }

    public Task(Long id, String title, String description, int priority, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}