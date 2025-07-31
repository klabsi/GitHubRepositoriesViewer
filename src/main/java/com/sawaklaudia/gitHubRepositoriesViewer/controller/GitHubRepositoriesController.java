package com.sawaklaudia.gitHubRepositoriesViewer.controller;

import com.sawaklaudia.gitHubRepositoriesViewer.model.Repo;
import com.sawaklaudia.gitHubRepositoriesViewer.service.GitHubRepositoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitHubRepositories")
public class GitHubRepositoriesController {

    private final GitHubRepositoriesService gitHubRepositoriesService;

    public GitHubRepositoriesController(GitHubRepositoriesService gitHubRepositoriesService) {
        this.gitHubRepositoriesService = gitHubRepositoriesService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Repo>> getRepositories(@PathVariable("username") String username) {
        var repositoriesList = gitHubRepositoriesService.getRepositories(username);
        return new ResponseEntity<>(repositoriesList,
                HttpStatus.OK
        );
    }
}
