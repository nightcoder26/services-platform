package com.backend.UniErrands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    @NotBlank
    private String urgency;

    @NotNull
    private Double price;

    @NotNull
    private Long requesterId;
}
