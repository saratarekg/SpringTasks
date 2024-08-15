package com.example.dependencyinjectiontask;

import org.example.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    void addCourse(Course course);

    void updateCourse(Course course);

    Course viewCourse(int id);

    List<Course> viewAllCourses();

    void deleteCourse(int id);
}
