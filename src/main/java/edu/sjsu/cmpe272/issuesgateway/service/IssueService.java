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

import java.util.List;

public interface IssueService {

    IssueResponse createIssue(CreateIssueRequest request);

    List<IssueResponse> listIssues(
            String state,
            String labels,
            int page,
            int perPage
    );

    IssueResponse getIssue(int number);

    IssueResponse updateIssue(
            int number,
            UpdateIssueRequest request
    );

    CommentResponse createComment(
            int number,
            CreateCommentRequest request
    );

    List<CommentResponse> listComments(int number);
}