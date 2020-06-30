# Shareby

### The aim of this project  :
- File sharing between users 
- Uploading file
- Downloading file


### Tech Stack:
- Spring Boot
- Java 1.8
- JPA
- Restfull 
- Lombok
- H2 Database
- Spring security
- Maven
- spring security
- Swagger (partially implemented) 


### Endpoint Exposed Endpoints:
- POST /api/register
- POST /api/v1/file/upload
- GET  /api/v1/file/{id}
- POST /api/v1/file/sharedData
- GET  /api/v1/file

### Tables:
- SHARED_DATA
- SHARED_DATA_TO_USERS
- UPLOAD_DATA
- USER

### Properties which needs to change for testing (application.properties)
- file.upload-path=/Users/sraju/Documents/file/
- spring.datasource.url=jdbc:h2:file:/Users/sraju/Documents