package edu.sjsu.cmpe272.issuesgateway.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
        String message,
        int status,
        OffsetDateTime timestamp,
        Map<String, String> details
) {
}