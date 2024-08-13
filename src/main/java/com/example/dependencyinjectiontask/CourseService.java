package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import org.example.CourseRecommender;
import org.example.Course;


@Service
public class CourseService {
    private CourseRecommender courseRecommender;

    public CourseService() {

    }

    @Autowired
    public void setCourseRecommender(@Qualifier("PythonRecommender") CourseRecommender courseRecommender) {
        this.courseRecommender = courseRecommender;
    }



    public void showRecommendedCourses() {
        List<Course> courses = courseRecommender.recommendedCourses();
        courses.forEach(course -> System.out.println(course));
    }
    List<Course> getRecommendedCourses(){
        return courseRecommender.recommendedCourses();
    }
}

