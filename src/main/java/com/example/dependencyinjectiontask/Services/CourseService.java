package com.example.dependencyinjectiontask.Services;

import com.example.dependencyinjectiontask.CourseMapper;
import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import org.example.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.CourseRecommender;
import org.example.Course;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
public class CourseService {
    private CourseRecommender courseRecommender;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    private CourseMapper courseMapper;


    @Autowired
    public void setCourseRecommender(@Qualifier("JavaRecommender") CourseRecommender courseRecommender) {
        this.courseRecommender = courseRecommender;
    }


    public List<CourseDTO> showRecommendedCourses() {
        List<Course> courses = courseRecommender.recommendedCourses();

        if(courses.isEmpty()) {
            throw new EntityNotFoundException("No courses found");
        }
        else{
        return courses.stream().limit(5).map(courseMapper::toCourseDTO).collect(Collectors.toList());
    }
    }


    public CourseDTO viewCourse(int id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            return courseMapper.toCourseDTO(course);
        } else {
            throw new EntityNotFoundException("Course with id " + id + " not found");
        }
    }

    public Course viewCourseC(int id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            return course;
        } else {
            throw new EntityNotFoundException("Course with id " + id + " not found");
        }
    }


    public CourseDTO addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getTitle() == null || course.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (course.getCredit() == null || course.getCredit() <= 0) {
            throw new IllegalArgumentException("Course credit must be a positive number and not empty");
        }
        Course addedCourse = courseRepository.save(course);
        return courseMapper.toCourseDTO(addedCourse);
    }




    public Course updateCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new EntityNotFoundException("Course not found with ID: " + course.getId());
        }
        Course updatedCourse = courseRepository.save(course);
        return updatedCourse;
    }


    public void deleteCourse(int id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Course with id " + id + " does not exist.");
        }
    }


    public Page<Course> viewAllCoursesPaginated(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);

        if (courses.isEmpty()) {
            throw new EntityNotFoundException("No courses found");
        }

        return courses;
    }


    }






