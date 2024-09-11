package com.example.dependencyinjectiontask.Controllers;


import com.example.dependencyinjectiontask.Services.CourseService;
import org.example.Course;
import org.example.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable int id) {
            CourseDTO course = courseService.viewCourse(id);
            return ResponseEntity.ok(course);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO addedCourse = courseService.addCourse(courseDTO);
        return ResponseEntity.ok(addedCourse);
    }

@PutMapping("/update/{id}")
public ResponseEntity<CourseDTO> updateCourse(
        @PathVariable("id") Long id,
        @Valid @RequestBody CourseDTO courseDTO) {

    CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
    return ResponseEntity.ok(updatedCourse);
}


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<CourseDTO>> discoverRecommendedCourses() {
            List<CourseDTO> courses = courseService.showRecommendedCourses();
            return ResponseEntity.ok(courses);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Course>> discoverAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
            Page<Course> courses = courseService.viewAllCoursesPaginated(pageable);
            return ResponseEntity.ok(courses);
    }
}
