package com.example.dependencyinjectiontask;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.example.CourseRecommender;
import org.example.Course;

import java.util.Arrays;
import java.util.List;
import org.example.CourseRecommender;
import org.example.Course;

public class JavaCourseRecommender implements CourseRecommender {
    @Override
        public List<Course> recommendedCourses() {
            return Arrays.asList(
                    new Course("Java 101", "Introduction to Java programming",4,2),
                    new Course("Advanced Java", "Deep dive into Java features",4,2),
                    new Course("Spring Framework", "Building applications with Spring",4,2)
            );
        }

}