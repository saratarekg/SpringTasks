package com.example.dependencyinjectiontask;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.sql.SQLException;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class DependencyInjectionTaskApplication {

    public static void main(String[] args) throws SQLException {


        ApplicationContext context = SpringApplication.run(DependencyInjectionTaskApplication.class, args);

        // Retrieve the CourseService bean from the application context
        CourseService courseService = context.getBean(CourseService.class);
        courseService.showRecommendedCourses();


    }

}
