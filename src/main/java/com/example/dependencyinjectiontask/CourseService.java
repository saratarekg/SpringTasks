package com.example.dependencyinjectiontask;

import jakarta.persistence.EntityNotFoundException;
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
        courses.forEach(course -> System.out.println(course));
        try{
        return courseRepository.findAll().stream().limit(5).map(courseMapper::toCourseDTO).collect(Collectors.toList());
    }
    catch(Exception e){
        throw new CustomException("Failed to fetch recommended courses", e);
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

    public CourseDTO addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        Course addedCourse = courseRepository.save(course);
        return courseMapper.toCourseDTO(addedCourse);
    }



    public CourseDTO updateCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new CustomException("Course not found with ID: " + course.getId());
        }
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toCourseDTO(updatedCourse);
    }


    public void deleteCourse(int id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Course with id " + id + " does not exist.");
        }
    }


    public Page<Course> viewAllCoursesPaginated(Pageable pageable) {
            return courseRepository.findAll(pageable);
//                    .map(courseMapper::toCourseDTO);

    } //to be tested yet




}

