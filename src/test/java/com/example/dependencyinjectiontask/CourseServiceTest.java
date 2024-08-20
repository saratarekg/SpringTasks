package com.example.dependencyinjectiontask;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.example.Course;
import org.example.CourseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CourseServiceTest {
    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseMapper courseMapper;

    @InjectMocks
    @Autowired
    CourseService courseService = new CourseService(courseRepository);


    @Test
    void viewCourse_courseExists_returnCourseDTO() {
        Course course = new Course("Java", "Java Description", 1);
        CourseDTO courseDTO = new CourseDTO("Java", "Java Description");

        when(courseRepository.findById(502)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);


        CourseDTO result = courseService.viewCourse(502);

        assertEquals(courseDTO.getTitle(), result.getTitle());
        assertEquals(courseDTO.getDescription(), result.getDescription());

    }

    @Test
    void viewCourse_courseNotExists_throwsEntityNotFoundException() {
        when(courseRepository.findById(111)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            courseService.viewCourse(111);
        });
    }


    @Test
    void addCourse_courseNotNull_returnCourseDTO() {
        Course course = new Course("Java", "Java Description", 4);
        CourseDTO courseDTO = new CourseDTO("Java", "Java Description");

        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);

        CourseDTO resultDTO = courseService.addCourse(course);

        assertNotNull(resultDTO);
        assertEquals(courseDTO.getTitle(), resultDTO.getTitle());
        assertEquals(courseDTO.getDescription(), resultDTO.getDescription());
        verify(courseRepository).save(course);
        verify(courseMapper).toCourseDTO(course);
    }

    @Test
    void addCourse_courseNull_throwsIllegalArgumentException() {
        Course nullCourse = null;
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(nullCourse));
    }

    @Test
    void updateCourse_courseExists_returnCourseDTO() {
        Course course = new Course(502,"Java","Java Description", 2);
        Course updatedCourse = new Course(502,"Java","Java Description",  2);
        CourseDTO updatedCourseDTO = new CourseDTO("Java", "Java Description");

        when(courseRepository.existsById(502)).thenReturn(true);
        when(courseRepository.save(course)).thenReturn(updatedCourse);
        when(courseMapper.toCourseDTO(updatedCourse)).thenReturn(updatedCourseDTO);

        CourseDTO result = courseService.updateCourse(course);

        assertEquals(updatedCourseDTO.getTitle(), result.getTitle());
        assertEquals(updatedCourseDTO.getDescription(), result.getDescription());

    }

    @Test
    void updateCourse_courseNotExists_throwsCustomException() {
        Course course = new Course(123,"test update","test update exception", 2);
        assertThrows(CustomException.class, () -> courseService.updateCourse(course));
    }

    @Test
    void deleteCourse_courseExists_deletesCourse() {
        int courseId = 502;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        courseService.deleteCourse(courseId);

        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void deleteCourse_courseNotExists_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> courseService.deleteCourse(1010));
    }
}
