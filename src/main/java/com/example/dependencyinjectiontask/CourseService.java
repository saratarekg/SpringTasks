package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import org.example.CourseRecommender;
import org.example.Course;
import org.springframework.stereotype.Service;


@Service
public class CourseService {
    private CourseRecommender courseRecommender;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

//    private JdbcTemplate jdbcTemplate;
//    public CourseService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }


    @Autowired
    public void setCourseRecommender(@Qualifier("JavaRecommender") CourseRecommender courseRecommender) {
        this.courseRecommender = courseRecommender;
    }


    public List<Course> showRecommendedCourses() {
        List<Course> courses = courseRecommender.recommendedCourses();
        courses.forEach(course -> System.out.println(course));
        return courses;
    }
    List<Course> getRecommendedCourses(){
        return courseRecommender.recommendedCourses();
    }


    public void addCourse(Course course) {
        courseRepository.addCourse(course);
    }

    public void updateCourse(Course course) {
        courseRepository.updateCourse(course);
    }

    public Course viewCourse(int id) {
        return courseRepository.viewCourse(id);
    }

    public List<Course> viewAllCourses() {
        return courseRepository.viewAllCourses();
    }

    public void deleteCourse(int id) {
        courseRepository.deleteCourse(id);
    }

}

