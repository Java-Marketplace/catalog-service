package com.jmp.catalogservice.support;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import com.jmp.catalogservice.support.initializer.PostgresInitializer;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("integration")
@RunWith(SpringRunner.class)
@Testcontainers
@ContextConfiguration(initializers = {PostgresInitializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@DBUnit(caseSensitiveTableNames = true, schema = "public")
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
}
