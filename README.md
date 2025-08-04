# GitHubRepositoriesViewer
___
This Spring Boot Application allows to get information about public non-forked GitHub repositories and their branches.

## Technologies:
- Java21+
- Spring Boot 3.x
- SpringWeb, Retrofit
- Spring Boot Test for integration tests

## Prerequisites:
- Java 21 or newer
- Gradle
- Internet access to GitHub API

## How to run the project:

#### 1. Clone the project
```bash 
git clone https://github.com/klabsi/GitHubRepositoriesViewer.git
cd GitHubRepositoriesViewer
```

####  2. Run the project

```bash
./gradlew bootRun
```

#### 3. Access the GET endpoint
 
Available endpoint:
```html
http://localhost:8080/repos/{username}  -> list non-forked repos, branches with last sha for GitHub user
```
#### Example response:
```json
[
   {
      "name":"repo-name",
      "owner":{
         "login":"owner-login"
      },
      "branches":[
         {
            "name":"main",
            "sha":"last-commit-sha"
         }
      ]
   }
]
```
## Error handling:
- Returns JSON error responses with status and message fields
```json
{
  "status": "${httpStatusCode}",
  "message": "${errorMessage}"
}
```

## Tests:
- Integration tests for controller endpoints using:
  - WireMock to run a local HTTP server that simulates GitHub API responses
  - TestRestTemplate to send an HTTP request to the controller and verify responses
  - Tests run with Spring Boot’s test support to load the full application context