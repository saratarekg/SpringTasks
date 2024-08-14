//package com.example.dependencyinjectiontask;
//
//import org.example.Course;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseSeeder {
//
//    @Bean
//    CommandLineRunner runner(CourseService courseService) {
//        return args -> {
//            // Add a new course
//            Course newCourse = new Course("","",0,0);
//            newCourse.setTitle("Spring Framework");
//            newCourse.setDescription("Learn the basics of Spring");
//            newCourse.setCredit(4);
//            newCourse.setAuthorId(1); // Assuming the author with ID 1 exists
//            courseService.addCourse(newCourse);
//
//            // View the course
//            Course course = courseService.viewCourse(1);
//            System.out.println("Course: " + course.getTitle());
//
//            // Update the course
//            course.setDescription("Learn the advanced concepts of Spring");
//            courseService.updateCourse(course);
//
//            // Delete the course
//            courseService.deleteCourse(1);
//        };
//    }
//}
