# TOEIC App (backend prototype)

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

H2 console:

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:toeicdb`
- User: `sa` (password empty)

確認: `passage`, `question`, `choice` テーブルにデータが入っています。
