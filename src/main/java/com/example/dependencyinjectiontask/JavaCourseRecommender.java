package com.example.dependencyinjectiontask;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Primary
@Component("JavaCourseRecommender")
public class JavaCourseRecommender implements CourseRecommender {
    @Override
        public List<Course> recommendedCourses() {
            return Arrays.asList(
                    new Course("Java 101", "Introduction to Java programming"),
                    new Course("Advanced Java", "Deep dive into Java features"),
                    new Course("Spring Framework", "Building applications with Spring")
            );
        }

}