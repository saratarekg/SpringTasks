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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void addCourse_noTitle_throwsIllegalArgumentException() {
        Course course = new Course(null, "Java Description", 4);
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(course));
    }

    @Test
    void addCourse_noCredit_throwsIllegalArgumentException() {
        Course course = new Course("Java", "Java Description", null);
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(course));
    }

    @Test
    void addCourse_negativeCredit_throwsIllegalArgumentException() {
        Course course = new Course("Java", "Java Description", -2);
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(course));
    }

    @Test
    void updateCourse_courseExists_returnCourseDTO() {
        Course course = new Course(502,"Java","Java Description", 2);
        Course updatedCourse = new Course(502,"Java","Java Description",  2);

        when(courseRepository.existsById(502)).thenReturn(true);
        when(courseRepository.save(course)).thenReturn(updatedCourse);

        Course result = courseService.updateCourse(course);

        assertEquals(updatedCourse.getTitle(), result.getTitle());
        assertEquals(updatedCourse.getDescription(), result.getDescription());
        assertEquals(updatedCourse.getCredit(), result.getCredit());

    }

    @Test
    void updateCourse_courseNotExists_throwsEntityNotFoundException() {
        Course course = new Course(123,"test update","test update exception", 2);
        assertThrows(EntityNotFoundException.class, () -> courseService.updateCourse(course));
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

    @Test
    void showRecommendedCourses_noCoursesFound_throwsEntityNotFoundException() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.showRecommendedCourses();
        });

        assertEquals("No courses found", exception.getMessage());
    }

    @Test
    void showRecommendedCourses_coursesFound_returnsCourseDTOList() {
        List<Course> courses = IntStream.range(1, 10)
                .mapToObj(i -> new Course(i, "Course " + i, "Description " + i, 3))
                .collect(Collectors.toList());

        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDTO> courseDTOs = courses.stream()
                .limit(5)
                .map(course -> new CourseDTO(course.getTitle(), course.getDescription()))
                .collect(Collectors.toList());

        when(courseRepository.findAll()).thenReturn(courses);
        for (int i = 0; i < 5; i++) {
            when(courseMapper.toCourseDTO(courses.get(i))).thenReturn(courseDTOs.get(i));
        }

        List<CourseDTO> result = courseService.showRecommendedCourses();

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(courseDTOs, result);
    }


    @Test
    void viewAllCoursesPaginated_CoursesFound_returnsPaginatedCourses() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Course> courses = IntStream.range(1, 10)
                .mapToObj(i -> new Course(i, "Course " + i, "Description " + i, 3))
                .collect(Collectors.toList());

        Page<Course> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(courseRepository.findAll(pageable)).thenReturn(coursePage);

        Page<Course> result = courseService.viewAllCoursesPaginated(PageRequest.of(page, size));

        assertNotNull(result);
        assertEquals(courses.size(), result.getTotalElements());
        assertEquals(courses, result.getContent());
    }


    @Test
    void viewAllCoursesPaginated_noCoursesFound_throwsEntityNotFoundException() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(courseRepository.findAll(pageable)).thenReturn(Page.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.viewAllCoursesPaginated(pageable);
        });

        assertEquals("No courses found", exception.getMessage());
    }
}
