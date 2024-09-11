package com.example.dependencyinjectiontask;


import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import com.example.dependencyinjectiontask.Services.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Course;
import org.example.CourseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;
    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-validation-report", "true");
    }

    private String createURLWithPort(String path) {
        return "http://localhost:" + port + "/api/courses/" + path;
    }

    private static void addAuth() {
        headers.set(HttpHeaders.AUTHORIZATION, "Admin");
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, name, description, credit) VALUES (101, 'CSEN101','Intro to programming', 4)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM course WHERE id='101'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testgetCourse() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                createURLWithPort("101"), HttpMethod.GET, entity, CourseDTO.class);

        CourseDTO courseResDTO = response.getBody();
        Course courseRes = courseMapper.toCourse(courseResDTO);
        String expected = "{\"title\":\"CSEN101\",\"description\":\"Intro to programming\",\"credit\":4}";

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, objectMapper.writeValueAsString(courseResDTO));
        assertNotNull(courseResDTO);
        assertThat(courseResDTO).usingRecursiveComparison().isEqualTo(courseService.viewCourse(101L));
    }

    @Test
    @Sql(statements = "DELETE FROM course WHERE name='New Course'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddCourse() throws JsonProcessingException {
        CourseDTO newCourse = new CourseDTO("New Course", "Description of the new course", 3);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(newCourse), headers);

        addAuth();

        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                createURLWithPort("add"), HttpMethod.POST, entity, CourseDTO.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, name, description, credit) VALUES (101, 'CSEN101','Intro to programming', 4)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM course WHERE id='101'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateCourse() throws JsonProcessingException {
        CourseDTO updatedCourse = new CourseDTO("CSEN101", "Updated description", 4);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(updatedCourse), headers);

        addAuth();

        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                createURLWithPort("update/101"), HttpMethod.PUT, entity, CourseDTO.class);

        assertEquals(200, response.getStatusCodeValue());
        Course updated = courseRepository.findById(101L).orElse(null);
        assertNotNull(updated);
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, name, description, credit) VALUES (101, 'CSEN101','Intro to programming', 4)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM course WHERE id='101'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteCourse() throws JsonProcessingException{
        addAuth();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                createURLWithPort("delete/101"), HttpMethod.DELETE, entity, Void.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(courseRepository.findById(101L).orElse(null));
    }

    @Test
    public void testDiscoverRecommendedCourses() throws JsonProcessingException{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                createURLWithPort("recommended"), HttpMethod.GET, entity, CourseDTO[].class);

        assertEquals(200, response.getStatusCodeValue());
        CourseDTO[] recommendedCourses = response.getBody();
        assertNotNull(recommendedCourses);
        assertTrue(recommendedCourses.length > 0);
    }


    @Test
    @Sql(statements = {
            "INSERT INTO course(id, name, description, credit) VALUES (101, 'CSEN101', 'Intro to programming', 4)",
            "INSERT INTO course(id, name, description, credit) VALUES (102, 'MATH101', 'Intro to math', 3)",
            "INSERT INTO course(id, name, description, credit) VALUES (103, 'PHYS101', 'Intro to physics', 4)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM course WHERE id IN (101, 102, 103)", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDiscoverAllCoursesPaginated() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                createURLWithPort("paginated?page=0&size=2"), HttpMethod.GET, entity,
                new ParameterizedTypeReference<Map<String, Object>>() {});

        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);

        List<Map<String, Object>> courses = (List<Map<String, Object>>) responseBody.get("content");

        assertNotNull(courses);
        assertEquals(2, courses.size());

        assertEquals(0, responseBody.get("number"));
        assertEquals(2, responseBody.get("size"));
    }
}