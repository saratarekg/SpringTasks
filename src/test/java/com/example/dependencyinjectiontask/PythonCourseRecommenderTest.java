package com.example.dependencyinjectiontask;

import org.example.Course;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PythonCourseRecommenderTest {

    @MockBean
    private PythonCourseRecommender pythonCourseRecommender;


    @Test
    void recommendedCourses_returnsExpectedCourses() {
        List<Course> courses = pythonCourseRecommender.recommendedCourses();

        assertNotNull(courses, "The recommended courses list should not be null");

        verify(pythonCourseRecommender).recommendedCourses();

    }
}
