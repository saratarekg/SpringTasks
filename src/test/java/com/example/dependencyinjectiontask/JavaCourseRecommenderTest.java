package com.example.dependencyinjectiontask;

import org.example.Course;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JavaCourseRecommenderTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private JavaCourseRecommender javaCourseRecommender;


    @Test
    void recommendedCourses_coursesFound_returnsCourses() {

        List<Course> courses = Arrays.asList(
                new Course(1, "Course 1", "Description 1", 3),
                new Course(2, "Course 2", "Description 2", 3)
        );
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = javaCourseRecommender.recommendedCourses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Course 1", result.get(0).getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void recommendedCourses_noCoursesFound_returnsEmptyList() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<Course> result = javaCourseRecommender.recommendedCourses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findAll();
    }
}