package com.example.dependencyinjectiontask;

import org.example.Course;
import org.example.CourseRecommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JavaCourseRecommender implements CourseRecommender {


    @Override
    public List<Course> recommendedCourses() {

//        return courseRepository.findAll().stream().limit(5).collect(Collectors.toList());

        return List.of();
    }
}
