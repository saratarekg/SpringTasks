package com.example.dependencyinjectiontask;

import org.example.Course;
import org.example.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable int id) {
        CourseDTO course = courseService.viewCourse(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<Void> addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(
            @PathVariable("id") int id,
            @RequestParam(value = "name", required = false) Optional<String> name,
            @RequestParam(value = "description", required = false) Optional<String> description,
            @RequestParam(value = "credit", required = false) Optional<Integer> credit,
            @RequestParam(value = "authorId", required = false) Optional<Integer> authorId) {

        try {
            CourseDTO courseDTO = courseService.viewCourse(id);
            Course course = courseMapper.toCourse(courseDTO);

            course.setTitle(name.orElse(course.getTitle()));
            course.setDescription(description.orElse(course.getDescription()));
            course.setCredit(credit.orElse(course.getCredit()));
//            course.setAuthorId(authorId.orElse(course.getAuthorId()));
            courseService.updateCourse(course);

//            courseService.updateCourse(id, name, description, credit, authorId);
            return new ResponseEntity<>("Course updated successfully", HttpStatus.OK);
        }  catch (Exception e) {
            // Log the exception and return an appropriate response
            return new ResponseEntity<>("Course not found or update failed", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id) {
        // Retrieve the course before deleting
        CourseDTO courseDTO = courseService.viewCourse(id);
        Course course = courseMapper.toCourse(courseDTO);
        if (course == null) {
            return ResponseEntity.notFound().build(); // Return 404 if course not found
        }
        courseService.deleteCourse(id);
        return ResponseEntity.ok(course); // Return the deleted course with 200 OK status
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<Course>> discoverRecommendedCourses() {
        List<Course> courses= courseService.showRecommendedCourses();
        return ResponseEntity.ok(courses);

    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CourseDTO>> discoverAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseDTO> courses = courseService.viewAllCoursesPaginated(pageable);
        return ResponseEntity.ok(courses);
    }

}
