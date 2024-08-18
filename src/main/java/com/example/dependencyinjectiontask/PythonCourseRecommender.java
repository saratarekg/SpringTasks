package com.example.dependencyinjectiontask;


import java.util.Arrays;
import java.util.List;
import org.example.CourseRecommender;
import org.example.Course;

public class PythonCourseRecommender implements CourseRecommender {
    @Override
    public List<Course> recommendedCourses() {
        return Arrays.asList(
                new Course("Python 101", "Introduction to Python programming",2),
                new Course("Advanced Python", "Deep dive into Java Python",2)
        );
    }
}