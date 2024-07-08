package com.egtdigitaltask.gateway;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTestContainers
{
    @Container
    public static final MySQLContainer<?> mysqlContainer;

    static
    {
        mysqlContainer = new MySQLContainer<>("mysql:8.1").withDatabaseName("egtdigital")
                                                                          .withUsername("user")
                                                                          .withPassword("secret")
                                                                          .withReuse(true);
        mysqlContainer.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry)
    {
        registry.add("spring.datasource.url", () -> mysqlContainer.getJdbcUrl() + "?currentSchema=egtdigital");
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword  );
    }

    @Test
    public void connectionEstablished()
    {
        Assertions.assertTrue(mysqlContainer.isCreated());
        Assertions.assertTrue(mysqlContainer.isRunning());
    }

}
