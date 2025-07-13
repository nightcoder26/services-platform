package com.backend.UniErrands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
public class TaskApproveHelperDTO {
     @NotNull
    private Long id;

    @NotBlank
    private String role;
}
