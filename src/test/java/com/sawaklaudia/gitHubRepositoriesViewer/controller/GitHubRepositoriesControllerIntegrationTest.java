package com.sawaklaudia.gitHubRepositoriesViewer.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.sawaklaudia.gitHubRepositoriesViewer.response.RepoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "github.api.base-url=http://localhost:${wiremock.server.port}"
})
class GitHubRepositoriesControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetAllNonForkedRepositories() {
        // given
        WireMock.stubFor(get(urlEqualTo("/users/johndoe/repos"))
                .willReturn(okJson("""
                        [
                            {
                                "name": "repo-one",
                                "owner": {
                                    "login": "johndoe"
                                },
                                "fork": false
                            },
                            {
                                "name": "repo-two",
                                "owner": {
                                    "login": "johndoe"
                                },
                                "fork": true
                            }
                        ]
                         """)));

        WireMock.stubFor(get(urlEqualTo("/repos/johndoe/repo-one/branches"))
                .willReturn(okJson("""
                        [
                            {
                                "name": "main",
                                "commit": {
                                    "sha": "1a2b3c"
                                }
                            }
                        ]""")));

        // when
        ResponseEntity<List<RepoResponse>> response = restTemplate.exchange(
                "/gitHubRepositories/johndoe",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RepoResponse> repos = response.getBody();
        assertEquals(1, repos.size());

        RepoResponse repo = repos.getFirst();
        assertEquals("repo-one", repo.name());
        assertEquals("johndoe", repo.login());
        assertEquals(1, repo.branches().size());
        assertEquals("main", repo.branches().getFirst().name());
        assertEquals("1a2b3c", repos.getFirst().branches().getFirst().commit().sha());
    }
}