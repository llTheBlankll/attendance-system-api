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

### Long-term TODO List
This section outlines areas that need attention, improvement, or future enhancements.

- [ ] **Enhance Logging:** Improve and expand logging statements for better debugging and monitoring.

- [ ] **Add Unit Tests:** Increase test coverage by adding unit tests for critical components and edge cases.

- [ ] **Optimize Cache Configuration:** Fine-tune cache settings based on performance testing and usage patterns.

- [ ] **Documentation Updates:** Keep the documentation up-to-date with any changes to the API, configurations, or endpoints.

- [ ] **Security Audit:** Conduct a security audit to identify and address potential vulnerabilities.

- [ ] **Improve Error Handling:** Enhance error handling mechanisms to provide more informative and user-friendly error messages.

- [ ] **Code Refactoring:** Identify and refactor sections of the codebase for improved readability and maintainability.

- [ ] **Performance Optimization:** Profile and optimize critical sections of the application for better performance.

- [ ] **Feedback Mechanism:** Establish a system for collecting user feedback and improvement suggestions.

- [ ] **Add Examples:** Provide more usage examples and code snippets in the documentation (**Spring Docs**).

- [x] **Add Rate Limiting:** Add Rate limiting to prevent DoS or DDoS.

- [x] **Add Performance/Error Reporting** Add Third Party Performance and Error Tracker like Sentry IO

### Additional TODO to be done soon.
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