package com.example.to_do_list.dto;

import com.example.to_do_list.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required (OPEN, IN_PROGRESS, DONE)")
    private Task.TaskStatus status;
}
