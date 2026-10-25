package com.corales.webdev2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:webdev2-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.show-sql=false"
})
class Webdev2ApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void detachedEmailChangeIsNotPersisted() {
		String storedEmail = jdbcTemplate.queryForObject(
				"SELECT email FROM app_users WHERE username = ?",
				String.class,
				"week7_user");

		assertEquals("week7@example.com", storedEmail);
	}

}
