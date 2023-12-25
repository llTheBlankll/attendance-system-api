# Attendance System API
<div align="center" style="display: flex; flex-direction: column; align-items: center;">
  
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=llTheBlankll_attendance-system-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=llTheBlankll_attendance-system-api)
![Dependabot](https://img.shields.io/badge/dependabot-025E8C?style=for-the-badge&logo=dependabot&logoColor=white)
[![wakatime](https://wakatime.com/badge/github/llTheBlankll/school-attendance-system.svg?style=plastic)](https://wakatime.com/badge/github/llTheBlankll/school-attendance-system)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
  
</div>

## Overview

This project is an Attendance System API built using Spring Boot, WebSocket, and JWT Authentication. The system provides a robust solution for tracking and managing attendance records, with support for real-time updates through WebSocket communication.

## Features

- **WebSocket Integration:** Real-time communication for instant updates on attendance events.
- **JWT Authentication:** Secure API access with JSON Web Token authentication.
- **Various Controllers:** The API includes the following controllers for managing different aspects of the system:

  - **User Controller:** Manages user-related functionalities such as creating, updating, and retrieving user information. Handles user roles and permissions.

  - **Authentication:** Provides endpoints for user authentication, login, and sign-up. Implements secure authentication using JWT tokens.

  - **Config:** Allows dynamic configuration changes to the server, enabling administrators to modify system settings on-the-fly.

  - **Gradelevel:** Facilitates CRUD operations for grade levels. Additionally, provides search functionalities for grade levels based on criteria such as name, academic year, and more.

  - **Guardian:** Manages information about students' guardians. Supports CRUD operations for creating, updating, and retrieving guardian details. Enables searching for guardians based on names, contact information, and relationships.

  - **Student:** Handles students' information comprehensively with CRUD operations. Extensive search functionalities include filtering by name, grade level, section, and other criteria.

  - **Section:** Manages information about sections, including CRUD operations for creating, updating, and retrieving section details. Supports searching for sections based on name, grade level, and capacity.

  - **Strand:** Provides CRUD operations for academic strands, allowing administrators to create, update, and retrieve strand information. Supports searching for strands by name and associated subjects.

  - **Teacher:** Manages teacher information, including full name, contact details, and subject assignments. Implements CRUD operations and provides search capabilities based on teacher name, subject, and other parameters.

  - **Subjects:** Manages subjects such as Mathematics, Science, and more. Enables CRUD operations for creating, updating, and retrieving subject details. Supports searching for subjects based on name, grade level, and teacher assignments.

  - **RFID Credentials:** Manages RFID authentication used by microcontrollers. Provides CRUD operations for registering, updating, and retrieving RFID credentials. Supports searching for RFID credentials based on device ID and associated users.

  - **Attendance:** Manages student attendance with CRUD operations and advanced search and filtering capabilities. Enables tracking attendance by student, date, subject, and other parameters.

  - **Attendance Statistics:** Provides detailed endpoints for obtaining attendance statistics for the entire school, students, and more. Includes CRUD operations and various capabilities like tracking daily, weekly, and monthly attendance, late arrivals, absences, and more.

## Technologies Used

- Spring Boot
- WebSocket
- JWT Authentication

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or later
- Maven

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/attendance-system-api.git
2. Navigate to the project directory:
   ```
   cd attendance-system-api
   ```
3. Import the database configuration at **src > main > resources > database.sql**
4. **Optionally**, you can import mock data at **src > main > resources > dummy data**
5. Edit spring boot configuration (application.properties) at **src > main > resources > application.properties**
   - Edit connection string, username, password to yours.
   - **Recommended:** Edit jwt.secret with secure SHA512 hash and the JWT Secret should not be less than 512 random characters and/or symbols.
   - Edit server.port if you want to listen it to another port. (Change when there's already an application listening on that port)
   - **NOTE**: If you change the api.root, also change springdoc.paths-to-match to be the same as the root.
   - You can edit the time for being late and flag ceremony.
6. Now build the application with this command:
   ```
   mvn clean package -DskipTests
   ```
7. Navigate to the folder where the jar file is created
   ```
   cd target
   ```
8. Run the application with:
   ```
   java -jar attendace-system-api-VERSION.jar
   ```
   Replace version with the version of the application you have cloned or downloaded.
9. Access the API at
   ```
   http://localhost:8080
   ```
   in my case, it was listening on Port 8080

## Detailed To-Do List

### 1. Define API Endpoints:

### User Controller:

- [x]  Create endpoints for creating, updating, and retrieving user information.
- [x]  Implement user roles and permissions.
- [x]  Enable password hashing for secure storage.

### Authentication:

- [x]  Develop endpoints for user authentication and sign in.
- [x]  Implement JWT token generation and verification for secure authentication.
- [x]  Include password strength validation during user registration.

### Config:

- [x]  Create endpoints for dynamic configuration changes.
- [x]  Implement security measures to restrict access to configuration endpoints.
- [x]  Allow administrators to update system settings, such as max capacity and attendance thresholds.

### Gradelevel:

- [x]  Implement CRUD operations for grade levels.
- [x]  Develop search functionalities based on criteria such as name, academic year, etc.
- [x]  Include API endpoints to retrieve a list of students in a specific grade level.

### Guardian:

- [x]  Enable CRUD operations for managing information about students’ guardians.
- [x]  Implement searching for guardians based on names, contact information, and relationships.
- [x]  Include an endpoint to retrieve a list of students and their guardians.

### Student:

- [x]  Develop comprehensive CRUD operations for managing students’ information.
- [x]  Implement extensive search functionalities, including filtering by name, grade level, section, etc.
- [ ]  Allow bulk import of student data using CSV or Excel files.

### Section:

- [x]  Implement CRUD operations for managing information about sections.
- [x]  Enable searching for sections based on name, grade level, and capacity.
- [x]  Include API endpoints to list students in a specific section.

### Strand:

- [x]  Develop CRUD operations for academic strands.
- [x]  Implement searching for strands by name and associated subjects.
- [x]  Allow administrators to assign strands to grade levels.

### Teacher:

- [x]  Create CRUD operations for managing teacher information.
- [x]  Implement search capabilities based on teacher name, subject, etc.
- [ ]  Include an endpoint to retrieve the list of subjects taught by a specific teacher.

### Subjects:

- [x]  Implement CRUD operations for managing subjects.
- [ ]  Enable searching for subjects based on name, grade level, and teacher assignments.
- [ ]  Include API endpoints to list subjects offered in a specific grade level.

### RFID Credentials:

- [x]  Develop CRUD operations for managing RFID credentials used by microcontrollers.
- [x]  Implement searching for RFID credentials based on device ID and associated users.
- [ ]  Include an endpoint to deactivate RFID credentials in case of loss or replacement.

### Attendance:

- [x]  Create CRUD operations for managing student attendance.
- [x]  Implement advanced search and filtering capabilities by student, date, subject, etc.
- [ ]  Allow teachers to mark attendance for their classes using the API.

### Attendance Statistics:

- [x]  Develop detailed endpoints for obtaining attendance statistics.
- [ ]  Include CRUD operations and capabilities for tracking daily, weekly, and monthly attendance, late arrivals, absences, etc.
- [ ]  Implement endpoints to retrieve attendance trends over academic terms.

### 2. Implement Security Measures:

- [x]  Ensure proper authentication and authorization mechanisms.
- [x]  Implement role-based access control to restrict access to sensitive endpoints.
- [ ]  Use HTTPS for secure communication.
- [x]  Validate and sanitize input data to prevent security vulnerabilities.
- [ ]  Implement rate limiting and IP blocking for potential security threats.

### 3. Implement Real-Time Communication:

- [x]  Integrate WebSocket for real-time updates on attendance events.
- [x]  Handle WebSocket connections, messages, and events.
- [x]  Implement WebSocket authentication to ensure secure communication.

### 4. Implement Testing:

- [ ]  Write unit tests for individual components.
- [ ]  Implement integration tests to ensure proper interaction between components.
- [ ]  Test API endpoints for different scenarios, including error cases.
- [ ]  Set up automated testing as part of the CI/CD pipeline.

### 5. Documentation:

- [x]  Generate API documentation using tools like Swagger or OpenAPI.
- [ ]  Provide clear and comprehensive documentation for API consumers.
- [ ]  Include examples of API requests and responses.
- [ ]  Create a developer guide for setting up and using the API.

### 6. Error Handling:

- [x]  Implement consistent error handling mechanisms for API responses.
- [x]  Provide meaningful error messages and status codes.
- [ ]  Include error logging for troubleshooting and debugging.

### 7. Performance Optimization:

- [x]  Optimize database queries for efficiency.
- [x]  Implement caching mechanisms for frequently accessed data.
- [ ]  Monitor and optimize the performance of WebSocket connections.
- [x]  Consider implementing asynchronous processing for resource-intensive tasks.

### 8. Logging:

- [x]  Implement logging for important events and errors.
- [x]  Use a logging framework to facilitate debugging and troubleshooting.
- [ ]  Configure log rotation and retention policies for efficient log management.

### 9. Deployment:

- [ ]  Choose an appropriate deployment strategy (e.g., Docker containers, cloud deployment). Only using Railway.app for now.
- [x]  Set up continuous integration and continuous deployment (CI/CD) pipelines.
- [ ]  Implement health checks and automated recovery mechanisms.

### 10. Monitoring and Analytics:

- [x]  Implement monitoring for the health and performance of the API.
- [x]  Set up analytics to track usage patterns and identify potential issues.
- [x]  Configure alerts for abnormal behavior or performance degradation.

### 11. Compliance and Regulations:

- [x]  Ensure compliance with data protection regulations (e.g., GDPR).
- [x]  Implement security best practices to protect user data.
- [x]  Document and adhere to relevant compliance standards for educational systems.

### 12. Usability and User Experience:

- [ ]  Design API responses to be user-friendly and easily consumable.
- [ ]  Consider implementing pagination for endpoints returning large datasets.
- [ ]  Gather feedback from API users to improve usability.

### 13. Future Considerations:

- [ ]  Plan for scalability as the user base grows.
- [ ]  Consider implementing versioning for API endpoints to support backward compatibility.
- [ ]  Explore the possibility of incorporating machine learning for predictive attendance analysis.### Additional TODO to be done soon.
- [ ] Attendance Statistics, get all absent students. Get by count, List of object and more.

### Authentication
Secure your API endpoints using JWT authentication. Include the JWT token in the request header:
```
Authorization: Bearer your_jwt_token
```

### Contributors
- [Vince Angelo Batecan](https://github.com/llTheBlankll/llTheBlankll)

### License
```
Copyright (c) 2012-2023 Scott Chacon and others

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```