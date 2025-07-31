package com.sawaklaudia.gitHubRepositoriesViewer.exception;

import com.sawaklaudia.gitHubRepositoriesViewer.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadGitHubUserInRequest(GitHubUserNotFoundException exception) {
        log.warn("not existing github user was provided");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .message("Provided GitHub user doesn't exist.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
