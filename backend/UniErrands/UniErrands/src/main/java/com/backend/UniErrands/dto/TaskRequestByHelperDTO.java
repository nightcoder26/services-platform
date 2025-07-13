package com.backend.UniErrands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestByHelperDTO {
    @NotNull
    private Long id;

    @NotBlank
    private String role;
}
