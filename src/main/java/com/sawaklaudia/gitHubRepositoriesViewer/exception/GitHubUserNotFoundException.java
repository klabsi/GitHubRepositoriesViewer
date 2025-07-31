package com.sawaklaudia.gitHubRepositoriesViewer.exception;

public class GitHubUserNotFoundException extends IllegalArgumentException {
    public GitHubUserNotFoundException(String message) {
        super(message);
    }
}
