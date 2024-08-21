package com.example.dependencyinjectiontask;

import org.example.Course;
import org.example.CourseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class CourseMapperTest {

    @Autowired
    private CourseMapper courseMapper;

    @Test
    void testToCourseDTO() {
        Course course = new Course(1, "Java Basics", "Intro to Java", 3);
        CourseDTO courseDTO = courseMapper.toCourseDTO(course);

        assertEquals(course.getTitle(), courseDTO.getTitle());
        assertEquals(course.getDescription(), courseDTO.getDescription());
    }

    @Test
    void testToCourse() {
        CourseDTO courseDTO = new CourseDTO( "Java Basics", "Intro to Java");
        Course course = courseMapper.toCourse(courseDTO);

        assertEquals(courseDTO.getTitle(), course.getTitle());
        assertEquals(courseDTO.getDescription(), course.getDescription());
    }
}
