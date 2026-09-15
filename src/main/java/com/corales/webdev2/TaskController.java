package com.corales.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    @PostMapping
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result) {

        if (result.hasErrors()) {
            return "task-form";
        }

        taskService.create(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Task task = taskService.findById(id);

        if (task == null) {
            return "redirect:/tasks";
        }

        model.addAttribute("task", task);
        return "task-form";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task task,
            BindingResult result) {

        if (result.hasErrors()) {
            return "task-form";
        }

        taskService.update(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }
}