package com.example.dependencyinjectiontask;

import javax.persistence.*;
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

    private CourseMapper courseMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable int id) {
        try {
            CourseDTO course = courseService.viewCourse(id);
            return ResponseEntity.ok(course);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Failed to get course due to an unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<Object> addCourse(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "description", required = false) Optional<String> description,
            @RequestParam(value = "credit", required = true) Integer credit) {
        try {
//
            Course course = new Course();
            course.setTitle(name);
            course.setDescription(description.orElse(null));
            course.setCredit(credit);
            CourseDTO addedCourse = courseService.addCourse(course);
            return ResponseEntity.ok(addedCourse);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to add course due to an unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable("id") int id,
            @RequestParam(value = "name", required = false) Optional<String> name,
            @RequestParam(value = "description", required = false) Optional<String> description,
            @RequestParam(value = "credit", required = false) Optional<Integer> credit)
    {

        try {
            Course course = courseService.viewCourseC(id);
            System.out.print(course);
            course.setTitle(name.orElse(course.getTitle()));
            course.setDescription(description.orElse(course.getDescription()));
            course.setCredit(credit.orElse(course.getCredit()));
//            courseService.updateCourse(course);

            return ResponseEntity.ok(courseService.updateCourse(course));
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to update course due to an unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/recommended")
    public ResponseEntity<Object> discoverRecommendedCourses() {
        try {
            List<CourseDTO> courses = courseService.showRecommendedCourses();
            return ResponseEntity.ok(courses);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Failed to fetch recommended courses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/paginated")
    public ResponseEntity<Object> discoverAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Course> courses = courseService.viewAllCoursesPaginated(pageable);
            return ResponseEntity.ok(courses);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Failed to fetch paginated courses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
