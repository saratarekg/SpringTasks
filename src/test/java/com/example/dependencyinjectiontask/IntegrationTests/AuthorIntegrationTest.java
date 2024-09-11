package com.example.dependencyinjectiontask.IntegrationTests;


import org.example.AuthorDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.add("Authorization", "Admin");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/api/authors/" + uri;
    }

    @Test
    @Sql(statements = "INSERT INTO author(id, name, email) VALUES (5, 'John Doe', 'john@example.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM author WHERE email='john@example.com'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAuthorByEmail() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<AuthorDTO> response = restTemplate.exchange(
                createURLWithPort("email?email=john@example.com"), HttpMethod.GET, entity, AuthorDTO.class);

        assertEquals(200, response.getStatusCodeValue());

        AuthorDTO authorDTO = response.getBody();
        assertNotNull(authorDTO);
        assertEquals("john@example.com", authorDTO.getEmail());
        assertEquals("John Doe", authorDTO.getName());
    }
}
