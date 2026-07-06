package com.sandip.git.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleRequestDTO {

    @Valid
    @NotNull(message = "Header cannot be null")
    private HeaderDTO header;

    private String message;
    private String accountNumber;
    private Double amount;
}
