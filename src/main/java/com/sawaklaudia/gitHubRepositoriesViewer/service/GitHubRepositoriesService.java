package com.sawaklaudia.gitHubRepositoriesViewer.service;

import com.sawaklaudia.gitHubRepositoriesViewer.exception.GitHubUserNotFoundException;
import com.sawaklaudia.gitHubRepositoriesViewer.model.Branch;
import com.sawaklaudia.gitHubRepositoriesViewer.model.Repo;
import com.sawaklaudia.gitHubRepositoriesViewer.response.RepoResponse;
import com.sawaklaudia.gitHubRepositoriesViewer.retrofit.GitHubApi;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubRepositoriesService {

    private final GitHubApi gitHubApi;

    public GitHubRepositoriesService(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public List<RepoResponse> getRepositories(String username) {
        Response<List<Repo>> response;
        try {
            response = gitHubApi.listRepos(username).execute();
            if (response.code() == 404) {
                throw new GitHubUserNotFoundException("Provided GitHub user doesn't exist.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error downloading repositories for " + username + ": " + e.getMessage());
        }

        return getRepos(response);
    }

    @NotNull
    private List<RepoResponse> getRepos(Response<List<Repo>> response) {
        List<Repo> allRepos = response.body();
        List<Repo> nonForkedRepos = filterOutForks(allRepos);

        List<RepoResponse> finalRepos = new ArrayList<>();
        for (Repo repo : nonForkedRepos) {
            String repoName = repo.name();
            String login = repo.owner().login();
            List<Branch> branches = getBranchesInfo(login, repoName);

            RepoResponse finalRepo = RepoResponse.builder()
                    .name(repoName)
                    .login(login)
                    .branches(branches)
                    .build();

            finalRepos.add(finalRepo);
        }

        return finalRepos;
    }

    private List<Repo> filterOutForks(List<Repo> repos) {
        return repos.stream()
                .filter(repo -> !repo.isForked())
                .collect(Collectors.toList());
    }

    private List<Branch> getBranchesInfo(String username, String repoName) {
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
