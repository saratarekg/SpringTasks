package com.example.dependencyinjectiontask.Components;

import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import org.example.Course;
import org.example.CourseRecommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JavaCourseRecommender implements CourseRecommender {
    private final CourseRepository courseRepository;

    @Autowired
    public JavaCourseRecommender(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> recommendedCourses() {

        List<Course> courses = courseRepository.findAll();
        if (!courses.isEmpty()) {
            return courses;
        } else {

        return List.of();
        }
    }
}
