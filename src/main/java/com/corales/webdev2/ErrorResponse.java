package com.corales.webdev2;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record ErrorResponse(int status, String message, Instant timestamp, List<ApiFieldError> errors) {
}
