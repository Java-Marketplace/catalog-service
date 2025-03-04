package com.jmp.template;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class CatalogServiceApplicationTests {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void mainBeanIsPresent() {
        assertThat(context.getBean(CatalogServiceApplication.class)).isNotNull();
    }
}

class TemplateMainMethodTest {
    @Test
    void testMain() {
        assertDoesNotThrow(() -> CatalogServiceApplication.main(new String[]{}));
    }
}