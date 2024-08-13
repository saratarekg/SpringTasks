package org.example;


import java.util.Arrays;
import java.util.List;

public class PythonCourseRecommenderNew implements CourseRecommender {
    @Override
    public List<Course> recommendedCourses() {
        return Arrays.asList(
                new Course("Python for data science", "Advanced Python applications in Data Science")
        );
    }
}