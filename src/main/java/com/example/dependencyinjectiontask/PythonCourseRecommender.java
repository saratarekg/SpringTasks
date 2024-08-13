package com.example.dependencyinjectiontask;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

public class PythonCourseRecommender implements CourseRecommender {
    @Override
    public List<Course> recommendedCourses() {
        return Arrays.asList(
                new Course("Python 101", "Introduction to Python programming"),
                new Course("Advanced Python", "Deep dive into Java Python")
        );
    }
}