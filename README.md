# WebDev 2 - Spring Boot Labs

## Project Information

- Framework: Spring Boot
- Build Tool: Maven
- Java Version: 17
- Server Port: 8080
- Shop Name: My Web Store
- Currency: PHP

## Week 7: Spring Boot + MySQL

The application connects to a local MySQL database named `webdev2_lab7`. Create it before starting the application:

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p -P 3307 -e "CREATE DATABASE webdev2_lab7;"
```

The application connects to MySQL on port `3307`. Set `MYSQL_USER` and `MYSQL_PASSWORD` in the environment if your local MySQL credentials differ from the defaults. For example, in PowerShell:

```powershell
$env:MYSQL_USER = "root"
$env:MYSQL_PASSWORD = "your-local-mysql-password"
.\mvnw.cmd spring-boot:run
```

The configured `create-drop` schema strategy creates the entity tables while the application is running and drops them when it shuts down. In another MySQL client session, verify the connection and mapping:

```sql
USE webdev2_lab7;
SHOW TABLES;
DESCRIBE app_users;
SHOW INDEX FROM app_users;
SELECT id, username, email, created_at FROM app_users;
```

The startup lifecycle demonstration stores a user with `week7@example.com`, detaches it, and changes its email in memory to `unsaved@example.com` without merging. The database row should retain `week7@example.com`.

## How to Build

Run:

```powershell
.\mvnw.cmd clean package
```