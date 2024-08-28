package com.example.dependencyinjectiontask.Controllers;


import com.example.dependencyinjectiontask.Services.CourseService;
import org.example.Course;
import org.example.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable int id) {
            CourseDTO course = courseService.viewCourse(id);
            return ResponseEntity.ok(course);

    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCourse(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description", required = false) Optional<String> description,
            @RequestParam(value = "credit") Integer credit) {

            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setTitle(name);
            courseDTO.setDescription(description.orElse(null));
            courseDTO.setCredit(credit);
            CourseDTO addedCourse = courseService.addCourse(courseDTO);
            return ResponseEntity.ok(addedCourse);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable("id") int id,
            @RequestParam(value = "name", required = false) Optional<String> name,
            @RequestParam(value = "description", required = false) Optional<String> description,
            @RequestParam(value = "credit", required = false) Optional<Integer> credit) {

            Course course = courseService.viewCourseC(id);
            course.setTitle(name.orElse(course.getTitle()));
            course.setDescription(description.orElse(course.getDescription()));
            course.setCredit(credit.orElse(course.getCredit()));
            Course updatedCourse = courseService.updateCourse(course);
            return ResponseEntity.ok(updatedCourse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {

            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");

    }

    @GetMapping("/recommended")
    public ResponseEntity<Object> discoverRecommendedCourses() {

            List<CourseDTO> courses = courseService.showRecommendedCourses();
            return ResponseEntity.ok(courses);

    }

    @GetMapping("/paginated")
    public ResponseEntity<Object> discoverAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
            Page<Course> courses = courseService.viewAllCoursesPaginated(pageable);
            return ResponseEntity.ok(courses);

    }
}
