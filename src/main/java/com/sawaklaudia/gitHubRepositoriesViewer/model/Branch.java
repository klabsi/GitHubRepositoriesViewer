package com.sawaklaudia.gitHubRepositoriesViewer.model;

import lombok.Builder;

@Builder
public record Branch (String name, Commit commit) {
}
