package com.corales.webdev2.controller;

import com.corales.webdev2.exception.TaskNotFoundException;
import com.corales.webdev2.model.Task;
import com.corales.webdev2.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "task-list";
    }

    @GetMapping("/tasks/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    @GetMapping("/tasks/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.findById(id));
        return "task-detail";
    }

    @PostMapping("/tasks")
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "task-form";
        }

        taskService.save(task);
        return "redirect:/tasks";
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public String handleTaskNotFound(TaskNotFoundException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "error/not-found";
    }
}
