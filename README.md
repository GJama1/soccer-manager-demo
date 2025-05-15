# Soccer Manager Demo Application

## Quickstart Guide

### Prerequisites
- Docker and Docker Compose installed on your system.

### Steps to Launch the Application
1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Run the following command to start the application:
   ```bash
   docker-compose up -d
   ```
4. The application will be available at: http://localhost:8080/swagger-ui.html

### Available Users
The following users are pre-configured in the system:

| Email                | Password |
|----------------------|----------|
| user1@example.com    | Aa12345. |
| user2@example.com    | Aa12345. |
| user3@example.com    | Aa12345. |

### Authorization
1. Open the Swagger UI at http://localhost:8080/swagger-ui.html.
2. Click the **Authorize** button.
3. Enter the following client credentials:
   - **Client ID**: `orbital`
   - **Client Secret**: `orbital`
4. Log in using one of the available user credentials listed above.