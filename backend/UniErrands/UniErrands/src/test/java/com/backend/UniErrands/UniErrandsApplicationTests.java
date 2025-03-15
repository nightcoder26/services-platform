package com.backend.UniErrands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class UniErrandsApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(UniErrandsApplicationTests.class);

	@Test
	void contextLoads() {
		logger.info("Application context loaded successfully");
	}

}
