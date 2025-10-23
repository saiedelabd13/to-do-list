package com.example.to_do_list.service;


import com.example.to_do_list.dto.TaskRequest;
import com.example.to_do_list.dto.TaskResponse;
import com.example.to_do_list.entity.Task;
import com.example.to_do_list.entity.User;
import com.example.to_do_list.exception.UnauthorizedException;
import com.example.to_do_list.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;


    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userId(task.getUser().getId())
                .build();
    }


    public TaskResponse createTask(TaskRequest request, User currentUser) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(Task.TaskStatus.OPEN) // Always start as OPEN on creation
                .user(currentUser)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }


    public List<TaskResponse> getAllUserTasks(User currentUser) {
        return taskRepository.findByUserId(currentUser.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, Task.TaskStatus newStatus, User currentUser) {
        Task task = taskRepository.findByIdAndUserId(taskId, currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("Task not found or you do not have permission to access this task."));

        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }


    public void deleteTask(Long taskId, User currentUser) {
        Task task = taskRepository.findByIdAndUserId(taskId, currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("Task not found or you do not have permission to delete this task."));

        taskRepository.delete(task);
    }
}
