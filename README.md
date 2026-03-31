# Spring Boot Project

This is a simple Spring Boot project with login and role-based access.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL database

## Database Setup

Create a MySQL database named `raildb` with user `root` and password `root`.

The application will automatically create/update tables on startup.

## Running the Application

To run the application:

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Login Credentials

- **Admin**: username `admin`, password `admin`
- **User**: username `user`, password `user`

## Access

- `/` - Welcome page
- `/login` - Login page
- `/home` - Home page after login
- `/admin` - Admin only page
- `/user` - User only page

## Building the Project

```bash
mvn clean package
```

## Testing

```bash
mvn test
```

## Debugging

To debug the application, run in debug mode:

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Then attach debugger to port 5005.