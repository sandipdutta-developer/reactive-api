package com.sandip.git.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleResponseDTO {

    private HeaderDTO header;
    private String status;
    private String transactionReference;
    private String responseMessage;
    private Long processingTimeMs;
}
