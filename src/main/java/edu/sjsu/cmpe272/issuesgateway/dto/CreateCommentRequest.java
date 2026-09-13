package edu.sjsu.cmpe272.issuesgateway.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(

        @NotBlank(message = "comment body is required")
        String body
) {
}