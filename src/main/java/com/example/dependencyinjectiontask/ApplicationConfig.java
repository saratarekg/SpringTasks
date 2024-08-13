package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.example.dependencyinjectiontask")
public class ApplicationConfig {
    @Bean(name="JavaRecommender")
    public CourseRecommender javaRecommender() {
        return new JavaCourseRecommender();
    }

    @Bean(name="PythonRecommender")
    public CourseRecommender pythonRecommender() {
        return new PythonCourseRecommender();
    }


}

