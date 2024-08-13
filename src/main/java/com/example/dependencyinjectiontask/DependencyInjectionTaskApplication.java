package com.example.dependencyinjectiontask;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class DependencyInjectionTaskApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//        SpringApplication.run(DependencyInjectionTaskApplication.class, args);
        CourseService courseService = context.getBean(CourseService.class);
        courseService.showRecommendedCourses();
//        System.out.println(courseService.getRecommendedCourses2());

    }

}
