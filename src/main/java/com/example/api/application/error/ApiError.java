package com.example.api.application.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({"errorCode", "errorMessage", "errorCause"})
public class ApiError {
    
    private String errorCode;
    
    private String errorMessage;
    
    private String errorCause;
}
