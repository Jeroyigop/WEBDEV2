# WebDev 2 - Spring Boot Labs

## Project Information

- Framework: Spring Boot
- Build Tool: Maven
- Java Version: 17
- Server Port: 8080
- Shop Name: My Web Store
- Currency: PHP

## Week 7: Spring Boot + MySQL

The application connects to a local MySQL database named `webdev2_lab7` on port `3307`. Create the database before starting the application:

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p -P 3307
```

At the MySQL prompt, run:

```sql
CREATE DATABASE IF NOT EXISTS webdev2_lab7;
```

Set the MySQL password in the same PowerShell terminal used to start the app. Do not commit your password:

```powershell
$env:MYSQL_USER = "root"
$env:MYSQL_PASSWORD = "your-local-mysql-password"
.\mvnw.cmd spring-boot:run
```

Hibernate uses `create-drop`: it creates the entity table on startup and drops it when the application shuts down. While the app is running, verify the table and lifecycle result in MySQL:

```sql
USE webdev2_lab7;
SHOW TABLES;
DESCRIBE app_users;
SHOW INDEX FROM app_users;
SELECT username, email FROM app_users WHERE username = 'lab7_user';
```

The startup runner creates and persists `week7_user` with `week7@example.com`, detaches it, then changes only the detached Java object's email to `changed@example.com`. It does not merge the entity, so the database value stays `week7@example.com`.

## Build and Test

```powershell
.\mvnw.cmd clean package
```

## Week 9: JPA Relationships and Capstone Model

The Week 9 model adds bidirectional `User`/`UserProfile` and `Department`/`Employee`
relationships, a `Student`/`Course` many-to-many represented by the `Enrollment`
association entity, and a Pet Shelter capstone model (`PetOwner`, `Pet`, `Tag`, and
`PetTag`). The Pet Shelter ERD and relationship decisions are documented in
[answers.md](answers.md).

Run the Week 9 persistence checks with:

```powershell
.\mvnw.cmd -Dtest=JpaRelationshipsIntegrationTests test
```

The test uses an in-memory H2 database. Hibernate SQL logging remains enabled for
the normal application configuration; the test also checks statement counts for
the lazy-loading, transactional, and fetch-join reads.
