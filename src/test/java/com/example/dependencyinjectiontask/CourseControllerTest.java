package com.example.dependencyinjectiontask;

import jakarta.persistence.EntityNotFoundException;
import org.example.CourseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        CourseDTO courseDTO = new CourseDTO("testUpdate", "test update");

        when(courseService.viewCourse(courseId)).thenReturn(courseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\":\"testUpdate\",\"description\":\"test update\"}"));
    }



@Test
    void getCourse_courseNotExists_returnsBadRequest() throws Exception {
        int courseId = 999;
        when(courseService.viewCourse(courseId)).thenThrow(new EntityNotFoundException("Course with id " + courseId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{id}", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
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



//    @Test
//    void addCourse() {
//    }
//
//    @Test
//    void updateCourse() {
//    }
//
//    @Test
//    void deleteCourse() {
//    }
//
//    @Test
//    void discoverRecommendedCourses() {
//    }
//
//    @Test
//    void discoverAllCourses() {
//    }
}