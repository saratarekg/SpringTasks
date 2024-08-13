package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private CourseRecommender courseRecommender;

    public CourseService() {

    }

    @Autowired
    public void setCourseRecommender(CourseRecommender courseRecommender) {
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

