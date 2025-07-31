package com.sawaklaudia.gitHubRepositoriesViewer.service;

import com.sawaklaudia.gitHubRepositoriesViewer.model.Branch;
import com.sawaklaudia.gitHubRepositoriesViewer.retrofit.GitHubApi;
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

    public List<Branch> getBranchesInfo(String username, String repoName) {
        try {
            List<Branch> branches = gitHubApi.listBranches(username, repoName).execute().body();

            return branches.stream()
                    .map(branch -> new Branch(branch.name(), branch.commit()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error downloading branches.");
        }
    }
}
