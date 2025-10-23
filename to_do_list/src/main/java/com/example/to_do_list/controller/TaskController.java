package com.example.to_do_list.controller;


import com.example.to_do_list.dto.TaskRequest;
import com.example.to_do_list.dto.TaskResponse;
import com.example.to_do_list.entity.Task;
import com.example.to_do_list.entity.User;
import com.example.to_do_list.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // Endpoint: POST /tasks/create task
    @PostMapping("/create task")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        TaskResponse response = taskService.createTask(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint: POST /tasks/show all tasks

    @GetMapping("/showalltasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal User currentUser) {
        List<TaskResponse> tasks = taskService.getAllUserTasks(currentUser);
        return ResponseEntity.ok(tasks);
    }
    // Endpoint: POST /tasks/update task

       @PutMapping("/updateTask/{id}")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam Task.TaskStatus status,
            @AuthenticationPrincipal User currentUser) {
        TaskResponse response = taskService.updateTaskStatus(id, status, currentUser);
        return ResponseEntity.ok(response);
    }

    // Endpoint: POST /tasks/delete Task

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        taskService.deleteTask(id, currentUser);
        return ResponseEntity.noContent().build();
    }
  }
