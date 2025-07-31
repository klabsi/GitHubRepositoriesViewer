package com.sawaklaudia.gitHubRepositoriesViewer.response;

import com.sawaklaudia.gitHubRepositoriesViewer.model.Branch;
import lombok.Builder;

import java.util.List;

@Builder
public record RepoResponse (String name, String login, List<Branch> branches) {
}
