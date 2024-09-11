package com.example.dependencyinjectiontask;

import javax.persistence.*;
import org.example.Author;
import org.example.Course;
import org.example.CourseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers=CourseController.class)
@ContextConfiguration(classes = CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @Test
    void getCourse_courseExists_returnsCourseDTO() throws Exception {
        int courseId = 502;
        CourseDTO courseDTO = any(CourseDTO.class);

        when(courseService.viewCourse(courseId)).thenReturn(courseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



@Test
    void getCourse_courseNotExists_returnsNotFound() throws Exception {
        int courseId = 999;
        when(courseService.viewCourse(courseId)).thenThrow(new EntityNotFoundException("Course with id " + courseId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course with id " + courseId + " not found"));
    }

    @Test
    void getCourse_unexpectedError_returnsInternalServerError() throws Exception {
        int courseId = 1;
        when(courseService.viewCourse(courseId)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to get course due to an unexpected error"));
    }

    @Test
    void addCourse_courseDetailsEntered_returnsCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Scalable", "build scalable apps",2);

        when(courseService.addCourse(any(Course.class))).thenReturn(courseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .param("name", "Scalable")
                        .param("description", "build scalable apps")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Scalable"))
                .andExpect(jsonPath("$.description").value("build scalable apps")) ;
    }

    @Test
    void addCourse_creditNegative_returnsBadRequest() throws Exception {
        when(courseService.addCourse(any(Course.class))).thenThrow(new IllegalArgumentException("Course credit must be a positive number and not empty"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .param("name", "cs101")
                        .param("description", "a cs course")
                        .param("credit", "-2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course credit must be a positive number and not empty"));
    }

    @Test
    void addCourse_emptyName_returnsBadRequest() throws Exception {
        when(courseService.addCourse(any(Course.class))).thenThrow(new IllegalArgumentException("Course title cannot be empty"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .param("name", "")
                        .param("description", "a cs course")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course title cannot be empty"));
    }

    @Test
    void addCourse_unexpectedError_returnsInternalServerError() throws Exception {
        when(courseService.addCourse(any(Course.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .param("name", "csen101")
                        .param("description", "a cs course")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to add course due to an unexpected error"));
    }

    @Test
    void updateCourse_courseExists_returnsUpdatedCourse() throws Exception {
        int courseId = 502;
        Course Course = new Course(courseId, "Java2", "Java Description", 4);
        Course updatedCourse = new Course(courseId, "Java3", "Java Description", 4);

        when(courseService.viewCourseC(courseId)).thenReturn(Course);
        when(courseService.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/{id}", courseId)
                        .param("name", "Java3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.title").value("Java3"))
                .andExpect(jsonPath("$.description").value("Java Description"))
                .andExpect(jsonPath("$.credit").value(4));
    }

    @Test
    void updateCourse_courseNotExists_returnsNotFound() throws Exception {
        int courseId = 999;

        when(courseService.viewCourseC(courseId)).thenThrow(new EntityNotFoundException("Course with id " + courseId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/{id}", courseId)
                        .param("name", "csen101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course with id " + courseId + " not found"));
    }


    @Test
    void updateCourse_internalServerError_returnsBadRequest() throws Exception {
        when(courseService.addCourse(any(Course.class))).thenThrow(new RuntimeException("Failed to update course due to an unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .param("name", "csen101")
                        .param("description", "a cs course")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to add course due to an unexpected error"));
    }

    @Test
    void deleteCourse_courseNotExists_returnsNotFound() throws Exception {
        int courseId = 999;

        doThrow(new EntityNotFoundException("Course with id " + courseId + " not found"))
                .when(courseService).deleteCourse(courseId); //because returns void

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course with id " + courseId + " not found"));
    }

    @Test
    void deleteCourse_courseExists_returnsOK() throws Exception {
        int courseId = 502;

        doNothing().when(courseService).deleteCourse(courseId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void discoverRecommendedCourses_catchesException_returnsNotFound() throws Exception {
        when(courseService.showRecommendedCourses()).thenThrow(new EntityNotFoundException("No courses found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/recommended")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void discoverRecommendedCourses_returnsRecommendedCourses() throws Exception {
        List<CourseDTO> recommendedCourses = Arrays.asList(
                new CourseDTO("Java Basics", "Introduction to Java programming",4),
                new CourseDTO("Advanced Java", "In-depth Java concepts and practices",4)
        );

        when(courseService.showRecommendedCourses()).thenReturn(recommendedCourses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/recommended")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(recommendedCourses.size()))
                .andExpect(jsonPath("$[0].title").value("Java Basics"))
                .andExpect(jsonPath("$[0].description").value("Introduction to Java programming"))
                .andExpect(jsonPath("$[1].title").value("Advanced Java"))
                .andExpect(jsonPath("$[1].description").value("In-depth Java concepts and practices"));
    }

    @Test
    void discoverRecommendedCourses_catchesException_returnsInternalServerError() throws Exception {
        when(courseService.showRecommendedCourses()).thenThrow(new RuntimeException("No courses found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/recommended")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void discoverAllCourses_paginationProvided_returnsCourses() throws Exception {
        int page = 0;
        int size = 10;

        List<Course> courseList = List.of(
                new Course(1, "Course 1", "Description 1", 3),
                new Course(2, "Course 2", "Description 2", 4)
        );

        Page<Course> coursePage = new PageImpl<>(courseList);

        when(courseService.viewAllCoursesPaginated(PageRequest.of(page, size))).thenReturn(coursePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/paginated")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(courseList.size()))
                .andExpect(jsonPath("$.content[0].title").value("Course 1"))
                .andExpect(jsonPath("$.content[1].title").value("Course 2"));
    }

    @Test
    void discoverAllCourses_catchesEntityNotFoundException_returnsNotFound() throws Exception {
        int page = 0;
        int size = 10;

        when(courseService.viewAllCoursesPaginated(PageRequest.of(page, size))).thenThrow(new EntityNotFoundException("No courses found"));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/paginated")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void discoverAllCourses_catchesException_returnsInternalServerError() throws Exception {
        int page = 0;
        int size = 10;

        when(courseService.viewAllCoursesPaginated(PageRequest.of(page, size))).thenThrow(new RuntimeException("Failed to fetch paginated courses."));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/paginated")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


}