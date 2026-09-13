package edu.sjsu.cmpe272.issuesgateway.dto;

import java.time.OffsetDateTime;

public record CommentResponse(
        long id,
        String body,
        Object user,
        OffsetDateTime createdAt,
        String htmlUrl
) {
}