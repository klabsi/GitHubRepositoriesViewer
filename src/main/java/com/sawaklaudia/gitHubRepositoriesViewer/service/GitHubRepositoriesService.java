package com.sawaklaudia.gitHubRepositoriesViewer.service;

import com.sawaklaudia.gitHubRepositoriesViewer.retrofit.GitHubApi;
import com.sawaklaudia.gitHubRepositoriesViewer.model.Repo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubRepositoriesService {

    private final GitHubApi gitHubApi;

    public GitHubRepositoriesService(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public String getRepositories(String username) {
        try {
            List<Repo> repos = gitHubApi.listRepos(username).execute().body();

            return repos.stream()
                    .map(repo -> repo.name())
                    .collect(Collectors.joining(", "));
        } catch (IOException e) {
            throw new RuntimeException("Error downloading user repositories.");
        }
    }
}
