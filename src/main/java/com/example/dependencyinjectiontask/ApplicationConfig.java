package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.example.CourseRecommender;
import org.example.PythonCourseRecommender;

@Configuration
@ComponentScan("com.example.dependencyinjectiontask")
@ComponentScan("org.example")
public class ApplicationConfig {
    @Bean(name="JavaRecommender")
    public CourseRecommender javaRecommender() {
        return new JavaCourseRecommender();
    }

    @Bean(name="PythonRecommender")
    public CourseRecommender pythonRecommender() {
        return new PythonCourseRecommender();
    }

    @Bean
    public CourseService courseService() {
        return new CourseService();
    }


}

