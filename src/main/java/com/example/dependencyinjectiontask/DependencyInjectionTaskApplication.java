package com.example.dependencyinjectiontask;


import org.example.Course;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DependencyInjectionTaskApplication {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        CourseService courseService = context.getBean(CourseService.class);
        courseService.showRecommendedCourses();

        courseService.testConnection();

        Course newCourse = new Course("","",0,0);

        newCourse.setTitle("Spring Framework");
        newCourse.setDescription("Learn the basics of Spring");
        newCourse.setCredit(4);
        newCourse.setAuthorId(1);
        courseService.addCourse(newCourse);


        // View  course
        Course course = courseService.viewCourse(6);
        System.out.println("Course: " + course.getTitle() + " Description: " + course.getDescription());

        // Update  course
        course.setDescription("Learn the advanced concepts of Spring");
        courseService.updateCourse(course);
        System.out.println("Updated Course: " + course.getTitle() + " Description: " + course.getDescription());


        // Delete  course
        courseService.deleteCourse(5);
//        courseService.viewAllCourses();







    }

}
