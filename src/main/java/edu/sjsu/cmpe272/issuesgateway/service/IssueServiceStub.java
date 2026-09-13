/*
 * Author: Sai Vineetha Tirumalla
 * Contribution: Temporary service implementation used
 * to test the Person A controller before GitHub integration.
 */



package edu.sjsu.cmpe272.issuesgateway.service;

import edu.sjsu.cmpe272.issuesgateway.dto.CommentResponse;
import edu.sjsu.cmpe272.issuesgateway.dto.CreateCommentRequest;
import edu.sjsu.cmpe272.issuesgateway.dto.CreateIssueRequest;
import edu.sjsu.cmpe272.issuesgateway.dto.IssueResponse;
import edu.sjsu.cmpe272.issuesgateway.dto.UpdateIssueRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class IssueServiceStub implements IssueService {

    @Override
    public IssueResponse createIssue(CreateIssueRequest request) {
        OffsetDateTime now = OffsetDateTime.now();

        return new IssueResponse(
                1,
                "https://github.com/example/example/issues/1",
                "open",
                request.title(),
                request.body(),
                request.labels() == null
                        ? Collections.emptyList()
                        : request.labels(),
                now,
                now
        );
    }

    @Override
    public List<IssueResponse> listIssues(
            String state,
            String labels,
            int page,
            int perPage
    ) {
        return List.of();
    }

    @Override
    public IssueResponse getIssue(int number) {
        OffsetDateTime now = OffsetDateTime.now();

        return new IssueResponse(
                number,
                "https://github.com/example/example/issues/" + number,
                "open",
                "Temporary issue",
                "Temporary body",
                List.of(),
                now,
                now
        );
    }

    @Override
    public IssueResponse updateIssue(
            int number,
            UpdateIssueRequest request
    ) {
        OffsetDateTime now = OffsetDateTime.now();

        return new IssueResponse(
                number,
                "https://github.com/example/example/issues/" + number,
                request.state() == null
                        ? "open"
                        : request.state().name(),
                request.title() == null
                        ? "Updated issue"
                        : request.title(),
                request.body(),
                List.of(),
                now,
                now
        );
    }

    @Override
    public CommentResponse createComment(
            int number,
            CreateCommentRequest request
    ) {
        return new CommentResponse(
                1,
                request.body(),
                null,
                OffsetDateTime.now(),
                "https://github.com/example/example/issues/" + number
        );
    }

    @Override
    public List<CommentResponse> listComments(int number) {
        return List.of();
    }
}