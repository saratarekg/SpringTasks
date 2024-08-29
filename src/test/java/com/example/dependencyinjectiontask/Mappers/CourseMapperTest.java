package com.example.dependencyinjectiontask.Mappers;

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
        Course course = new Course("Java Basics", "Intro to Java", 3);
        CourseDTO courseDTO = courseMapper.toCourseDTO(course);

        assertEquals(course.getTitle(), courseDTO.getTitle());
        assertEquals(course.getDescription(), courseDTO.getDescription());
        assertEquals(course.getCredit(), courseDTO.getCredit());

    }

    @Test
    void testToCourse() {
        CourseDTO courseDTO = new CourseDTO( "Java Basics", "Intro to Java",4);
        Course course = courseMapper.toCourse(courseDTO);

        assertEquals(courseDTO.getTitle(), course.getTitle());
        assertEquals(courseDTO.getDescription(), course.getDescription());
        assertEquals(courseDTO.getCredit(), course.getCredit());

    }
}
