package com.example.dependencyinjectiontask.Services;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityNotFoundException;

import com.example.dependencyinjectiontask.Components.JavaCourseRecommender;
import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import org.example.Course;
import org.example.CourseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CourseServiceTest {
    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    JavaCourseRecommender courseRecommender;


    @InjectMocks
    @Autowired
    CourseService courseService = new CourseService(courseRepository);


    @Test
    void viewCourse_courseExists_returnCourseDTO() {
        Course course = new Course("Java", "Java Description", 1);
        CourseDTO courseDTO = new CourseDTO("Java", "Java Description",1);

        when(courseRepository.findById(502L)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);


        CourseDTO result = courseService.viewCourse(502);

        assertEquals(courseDTO.getTitle(), result.getTitle());
        assertEquals(courseDTO.getDescription(), result.getDescription());

    }

    @Test
    void viewCourse_courseNotExists_throwsEntityNotFoundException() {
        when(courseRepository.findById(111L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            courseService.viewCourse(111);
        });
    }


    @Test
    void addCourse_courseNotNull_returnCourseDTO() {
        Course course = new Course("Java", "Java Description", 4);
        CourseDTO courseDTO = new CourseDTO("Java", "Java Description",4);
        when(courseMapper.toCourse(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);

        CourseDTO resultDTO = courseService.addCourse(courseDTO);

        assertNotNull(resultDTO);
        assertEquals(courseDTO.getTitle(), resultDTO.getTitle());
        assertEquals(courseDTO.getDescription(), resultDTO.getDescription());
        verify(courseRepository).save(course);
        verify(courseMapper).toCourseDTO(course);
    }


    @Test
    void updateCourse_courseExists_returnCourseDTO() {
        Long courseId = 1L;
        Course existingCourse = new Course(Math.toIntExact(courseId), "Old Title", "Old Description", 2);
        Course updatedCourse = new Course(Math.toIntExact(courseId), "New Title", "New Description", 3);
        CourseDTO updateDTO = new CourseDTO("New Title", "New Description", 3);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(courseMapper.toCourseDTO(updatedCourse)).thenReturn(updateDTO);

        CourseDTO result = courseService.updateCourse(courseId, updateDTO);

        assertNotNull(result, "The result should not be null");
        assertEquals("New Title", result.getTitle(), "The title should be updated");
        assertEquals("New Description", result.getDescription(), "The description should be updated");
        assertEquals(3, result.getCredit(), "The credit should be updated");

        assertEquals(result,updateDTO);

        verify(courseMapper).toCourseDTO(updatedCourse);
    }




    @Test
    void updateCourse_courseNotExists_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> courseService.updateCourse(1L,new CourseDTO()));
    }

    @Test
    void deleteCourse_courseExists_deletesCourse() {
        Long courseId = 502L;
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
        when(courseRecommender.recommendedCourses()).thenReturn(Collections.emptyList());
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());


        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.showRecommendedCourses();
        });

        assertEquals("No courses found", exception.getMessage());

        verify(courseRepository).findAll();

    }

    @Test
    void showRecommendedCourses_coursesFound_returnsCourseDTOList() {
        List<Course> courses = IntStream.range(1, 10)
                .mapToObj(i -> new Course("Course " + i, "Description " + i, 3))
                .collect(Collectors.toList());

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> first5Courses = courses.stream()
                .limit(5)
                .collect(Collectors.toList());


        when(courseRecommender.recommendedCourses()).thenReturn(first5Courses);


        List<CourseDTO> courseDTOs = first5Courses.stream()
                .map(course -> new CourseDTO(course.getTitle(), course.getDescription(), course.getCredit()))
                .collect(Collectors.toList());


        for (int i = 0; i < 5; i++) {
            when(courseMapper.toCourseDTO(courses.get(i))).thenReturn(courseDTOs.get(i));
        }

        List<CourseDTO> result = courseService.showRecommendedCourses();

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(courseDTOs, result);

        verify(courseRepository).findAll();


    }


    @Test
    void viewAllCoursesPaginated_CoursesFound_returnsPaginatedCourses() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Course> courses = IntStream.range(1, 10)
                .mapToObj(i -> new Course( "Course " + i, "Description " + i, 3))
                .collect(Collectors.toList());

        Page<Course> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(courseRepository.findAll(pageable)).thenReturn(coursePage);

        Page<Course> result = courseService.viewAllCoursesPaginated(PageRequest.of(page, size));

        assertNotNull(result);
        assertEquals(courses.size(), result.getTotalElements());
        assertEquals(courses, result.getContent());

        verify(courseRepository).findAll(pageable);

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

        verify(courseRepository).findAll(pageable);

    }
}
