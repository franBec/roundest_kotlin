# roundest_kotlin

## Project Overview
This project is implemented in **Java Spring Boot 3** and provides a RESTful API for "The roundest Pokémon" type exercise.

## API Documentation
The OpenAPI documentation for the API can be found in the [src/main/resources/openapi/roundest.yaml file](https://github.com/franBec/roundest_kotlin/blob/main/src/main/resources/openapi/roundest.yaml).

## Getting Started

### Prerequisites
- For running with Maven:
    - **Java 21**
    - **Kotlin 1.9+**
    - **Maven**
- For containerized deployment
    - **Docker**

### Run with Maven
1. Clone the repository:
    ```bash
    git clone https://github.com/franBec/roundest_kotlin
    ```
2. Navigate to the project directory:
    ```bash
    cd roundest_kotlin
    ```
3. Build and run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
### Run with Docker
1. Clone the repository:
     ```bash
     git clone https://github.com/franBec/roundest_kotlin
     ```
2. Navigate to the project directory:
    ```bash
    cd roundest_kotlin
    ```
3. Build the Docker image:
    ```bash
    docker build -t roundest_kotlin .
    ```
4. Run the Docker container:
     ```bash
     docker run -p 8080:8080 roundest_kotlin
     ```

## Author
Franco Exequiel Becvort <🐤/>
- [Linkedin](https://www.linkedin.com/in/franco-becvort/)
- [Website](https://pollito.dev/)

This project is intended for educational purposes.