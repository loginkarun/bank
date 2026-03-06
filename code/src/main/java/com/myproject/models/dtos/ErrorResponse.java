package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String traceId;
    private String errorCode;
    private String message;
    private List<ErrorDetail> details = new ArrayList<>();

    public ErrorResponse(LocalDateTime timestamp, String traceId, String errorCode, String message) {
        this.timestamp = timestamp;
        this.traceId = traceId;
        this.errorCode = errorCode;
        this.message = message;
    }
}