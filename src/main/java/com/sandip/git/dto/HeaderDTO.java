package com.sandip.git.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDTO {

    @NotBlank(message = "msgId is mandatory")
    private String msgId;

    @NotBlank(message = "channelId is mandatory")
    private String channelId;

    private String timestamp;
}