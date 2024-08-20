//package com.example.dependencyinjectiontask;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.example.Course;
//import org.example.CourseDTO;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class CourseServiceTests {
//    @Mock
//    private CourseRepository courseRepository;
//
//    @Autowired
//    private CourseService courseService;
//
//    @Autowired
//    private CourseMapper courseMapper;
//
//
//    @Test
//    void viewCourse_courseExists_returnCourseDTO() {
//        Course course = new Course("Java", "Java Description", 1);
//        CourseDTO courseDTO = courseMapper.toCourseDTO(course);
//        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
//
//        CourseDTO result = courseService.viewCourse(1);
//
//        assertEquals(courseDTO, result);
//    }
//
//    @Test
//    void viewCourse_courseNotExists_throwsEntityNotFoundException() {
//        when(courseRepository.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            courseService.viewCourse(1);
//        });
//    }
//
//
//    @Test
//    void addCourse_courseNotNull_returnCourseDTO() {
//        Course course = new Course("Java", "Java Description", 1);
//        CourseDTO courseDTO = new CourseDTO("Java", "Java Description");
//
//        when(courseRepository.save(course)).thenReturn(course);
//        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);
//
//        CourseDTO resultDTO = courseService.addCourse(course);
//
//        assertNotNull(resultDTO);
//        assertEquals(courseDTO.getTitle(), resultDTO.getTitle());
//        assertEquals(courseDTO.getDescription(), resultDTO.getDescription());
//        verify(courseRepository).save(course);
//        verify(courseMapper).toCourseDTO(course);
//    }
//
//    @Test
//    void addCourse_courseNull_throwsIllegalArgumentException() {
//        Course nullCourse = null;
//        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(nullCourse));
//    }
//
//}
