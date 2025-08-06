# GitHubRepositoriesViewer
This Spring Boot Application allows users to get information about public non-forked GitHub repositories and their branches.

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

#### 3. (Optional) Build the JAR file
If you just want to build the application:
```bash
./gradlew bootJar
```
The .jar file will be created in the build/libs/ folder. To run it, use the command:
```bash
java -jar build/libs/gitHubRepositoriesViewer-0.0.1-SNAPSHOT.jar
```

#### 3. Test the GET endpoint
The available endpoint allows listing non-forked repositories and branches with the last commit SHA for a GitHub user.
After launching the application, the endpoint will be available at:
```html
http://localhost:8080/gitHubRepositories/{username}
```
From the terminal:
```bash
curl --location 'http://localhost:8080/gitHubRepositories/{username}'
```
#### Example response:
```json
[
   {
      "name": "repo-name",
      "owner": {
         "login": "owner-login"
      },
      "branches": [
         {
            "name": "main",
            "sha": "last-commit-sha"
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
