package com.example.dependencyinjectiontask.Configurations;

import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import com.example.dependencyinjectiontask.Components.JavaCourseRecommender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.example.CourseRecommender;
import org.example.PythonCourseRecommender;


@Configuration
@ComponentScan("com.example.dependencyinjectiontask")
@ComponentScan("org.example")
public class ApplicationConfig {


    @Bean(name = "JavaRecommender")
    public CourseRecommender javaRecommender(CourseRepository courseRepository) {
        return new JavaCourseRecommender(courseRepository);
    }

    @Bean(name="PythonRecommender")
    public CourseRecommender pythonRecommender() {
        return new PythonCourseRecommender();
    }





}

