package com.sawaklaudia.gitHubRepositoriesViewer.service;

import com.sawaklaudia.gitHubRepositoriesViewer.exception.GitHubBranchesDownloadException;
import com.sawaklaudia.gitHubRepositoriesViewer.exception.GitHubUserNotFoundException;
import com.sawaklaudia.gitHubRepositoriesViewer.exception.NetworkRequestException;
import com.sawaklaudia.gitHubRepositoriesViewer.model.Branch;
import com.sawaklaudia.gitHubRepositoriesViewer.model.Repo;
import com.sawaklaudia.gitHubRepositoriesViewer.response.RepoResponse;
import com.sawaklaudia.gitHubRepositoriesViewer.retrofit.GitHubApi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GitHubRepositoriesService {

    private final GitHubApi gitHubApi;

    public GitHubRepositoriesService(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public List<RepoResponse> getRepositories(String username) {
        log.info("downloading repositories for '{}'", username);
        Response<List<Repo>> response;
        try {
            response = gitHubApi.listRepos(username).execute();
            if (response.code() == 404) {
                log.warn("provided GitHub user doesn't exist: {}", username);
                throw new GitHubUserNotFoundException("Provided GitHub user doesn't exist: " + username);
            }
        } catch (IOException e) {
            log.error("error downloading {}'s repositories: ", username, e);
            throw new NetworkRequestException("Error downloading repositories for "
                    + username + ": " + e.getMessage());
        }

        return getRepos(response);
    }

    @NotNull
    private List<RepoResponse> getRepos(Response<List<Repo>> response) {
        List<Repo> allRepos = response.body();

        if(allRepos == null) {
            return Collections.emptyList();
        }

        return mapNotForkedReposToResponses(allRepos);
    }

    @NotNull
    private List<RepoResponse> mapNotForkedReposToResponses(List<Repo> allRepos) {
        return allRepos.stream()
                .filter(repo -> !repo.isForked())
                .map(repo -> {
                    String repoName = repo.name();
                    String login = repo.owner().login();
                    List<Branch> branches = getBranchesInfo(login, repoName);

                    return RepoResponse.builder()
                            .name(repoName)
                            .login(login)
                            .branches(branches)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Branch> getBranchesInfo(String username, String repoName) {
        log.info("downloading branches for '{}' from repository '{}'", username, repoName);
        List<Branch> branches;
        try {
            branches = gitHubApi.listBranches(username, repoName).execute().body();
        } catch (IOException e) {
            log.error("error downloading branches for user '{}' from repository '{}'", username, repoName, e);
            throw new GitHubBranchesDownloadException("Error downloading branches:" + e.getMessage());
        }

        if(branches == null) {
            return Collections.emptyList();
        }

        return branches.stream()
                .map(branch -> Branch.builder()
                        .name(branch.name())
                        .commit(branch.commit())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
