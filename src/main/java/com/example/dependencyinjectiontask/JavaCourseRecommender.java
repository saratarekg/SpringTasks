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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JavaCourseRecommender(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Course> recommendedCourses() {
        String sql = "SELECT * FROM Course";

        // Retrieve all courses
        List<Course> courses = jdbcTemplate.query(sql, new JdbcCourseRepository.CourseRowMapper());

        return courses.stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
