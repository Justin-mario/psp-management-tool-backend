# psp-management-tool-backend
# PSP Management Tool

A web-based Personal Software Process (PSP) management tool designed to enhance developer productivity and improve project time estimation accuracy.

## Features

- User Management
- Project Management
- Time Tracking
- Bug Logging
- PSP Guidelines and Tutorials
- Data Visualization
- Collaboration Features

## Technologies Used

- Java 21
- Spring Boot 3.2.3
- PostgreSQL
- React (frontend)
- Maven

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- Maven
- PostgreSQL

### Environment Setup

1. Clone the repository:https://github.com/Justin-mario/psp-management-tool-backend.git
2. Navigate to the project directory: cd psp-management-tool
3. Set up environment variables:
   Create a `.env` file in the project root with the following contents:DB_USERNAME=your_database_username
   DB_PASSWORD=your_database_password
4. Replace `your_database_username` and `your_database_password` with your actual PostgreSQL credentials.

4. Create a PostgreSQL database named `psp_management_db`.

### Running the Application

1. Build the project: mvn clean install
2. Run the application: mvn spring-boot:run
3. The application will be available at `http://localhost:8080`

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- Personal Software Process (PSP) methodology
- Spring Boot and the Spring community
- PostgreSQL