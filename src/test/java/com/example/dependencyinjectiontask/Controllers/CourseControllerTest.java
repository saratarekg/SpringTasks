package com.example.dependencyinjectiontask.Controllers;

import javax.persistence.EntityNotFoundException;

import com.example.dependencyinjectiontask.GlobalExceptionHandler;
import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import com.example.dependencyinjectiontask.Services.CourseService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers= CourseController.class)
@ContextConfiguration(classes = {CourseController.class, GlobalExceptionHandler.class})
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
        CourseDTO courseDTO = new CourseDTO("testUpdate", "test update",4);

        when(courseService.viewCourse(courseId)).thenReturn(courseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\":\"testUpdate\",\"description\":\"test update\"}"));
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
        when(courseService.viewCourse(courseId)).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));
    }

    @Test
    void addCourse_courseDetailsEntered_returnsCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO("Scalable", "build scalable apps", 2);

        when(courseService.addCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Scalable\",\"description\":\"build scalable apps\",\"credit\":2}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Scalable"))
                .andExpect(jsonPath("$.description").value("build scalable apps"))
                .andExpect(jsonPath("$.credit").value(2));
    }

@Test
void addCourse_invalidCourseDetails_returnsBadRequest() throws Exception {
    String invalidCourseJson = """
    {
        "title": "",
        "description": "Sample description",
        "credit": 0
    }
    """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/courses/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidCourseJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Course title cannot be empty"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.credit").value("Course credit must be a positive number"));
}

    @Test
    void addCourse_unexpectedError_returnsInternalServerError() throws Exception {
        when(courseService.addCourse(any(CourseDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses/add")
                        .param("name", "csen101")
                        .param("description", "a cs course")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));
    }

    @Test
    void updateCourse_courseExists_returnsUpdatedCourse() throws Exception {
        int courseId = 502;
        Course Course = new Course( courseId,"Java2", "Java Description", 4);
        Course updatedCourse = new Course(courseId,"Java3", "Java Description", 4);

        when(courseService.viewCourseC(courseId)).thenReturn(Course);
        when(courseService.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/update/{id}", courseId)
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/update/{id}", courseId)
                        .param("name", "csen101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course with id " + courseId + " not found"));
    }


    @Test
    void updateCourse_internalServerError_returnsBadRequest() throws Exception {
        when(courseService.addCourse(any(CourseDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/update/{id}", 1)
                        .param("name", "csen101")
                        .param("description", "a cs course")
                        .param("credit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));
    }

    @Test
    void deleteCourse_courseNotExists_returnsNotFound() throws Exception {
        int courseId = 999;

        doThrow(new EntityNotFoundException("Course with id " + courseId + " not found"))
                .when(courseService).deleteCourse(courseId); //because returns void

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/delete/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course with id " + courseId + " not found"));
    }

    @Test
    void deleteCourse_courseExists_returnsOK() throws Exception {
        int courseId = 502;

        doNothing().when(courseService).deleteCourse(courseId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/delete/{id}", courseId)
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
        when(courseService.showRecommendedCourses()).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/recommended")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));

    }

    @Test
    void discoverAllCourses_paginationProvided_returnsCourses() throws Exception {
        int page = 0;
        int size = 10;

        List<Course> courseList = List.of(
                new Course( 1,"Course 1", "Description 1", 3),
                new Course(2,"Course 2", "Description 2", 4)
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

        when(courseService.viewAllCoursesPaginated(PageRequest.of(page, size))).thenThrow(new RuntimeException());


        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/paginated")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));

    }


}