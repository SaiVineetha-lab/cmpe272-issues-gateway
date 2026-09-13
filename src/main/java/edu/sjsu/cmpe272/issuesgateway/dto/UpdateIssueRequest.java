package edu.sjsu.cmpe272.issuesgateway.dto;

import jakarta.validation.constraints.Size;

public record UpdateIssueRequest(

        @Size(
                min = 1,
                max = 256,
                message = "title must contain between 1 and 256 characters"
        )
        String title,

        String body,

        IssueState state
) {
}