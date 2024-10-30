# Useless Facts Application

This is a Kotlin application that fetches random facts from the Useless Facts API and provides endpoints to retrieve these facts. The application is built using Ktor and supports basic HTTP routing.

## Prerequisites

- **JDK**: Java Development Kit (JDK) version 17 or later.

## Getting Started

1. **Build the Project**:

   ```bash
   ./gradlew build  # For Unix-based systems
   gradlew.bat build  # For Windows

2. **Run the Application**:

   ```bash
   ./gradlew run  # For Unix-based systems
   gradlew.bat run  # For Windows


3. **Access the API**:

   Once the application is running, you can access the API endpoints:
   - **POST http://localhost:8080/facts**: Fetches a random fact.
   - **GET http://localhost:8080/facts/{shortenedUrl}**: Retrieves the fact corresponding to the shortened URL.
   - **GET http://localhost:8080/facts**: Retrieves all the facts stored.
   - **GET http://localhost:8080/facts/{shortenedUrl}/redirect**: Redirects to the original fact URL.
   - **GET http://localhost:8080/admin/statistics**: Retrieves access statistics.


## Running Tests
To run the unit tests, use the following command:

   ```bash
   ./gradlew test  # For Unix-based systems
   gradlew.bat test  # For Windows
