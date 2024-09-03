package com.example.dependencyinjectiontask.Configurations;

import com.example.dependencyinjectiontask.Components.JavaCourseRecommender;
import com.example.dependencyinjectiontask.Components.MockServerCourseRecommender;
import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.example.CourseRecommender;
import org.example.PythonCourseRecommender;
import org.springframework.web.client.RestTemplate;


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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "MockServerCourseRecommender")
    public CourseRecommender mockServerCourseRecommender(CourseMapper courseMapper) {
        return new MockServerCourseRecommender(restTemplate(),courseMapper);
    }

}

