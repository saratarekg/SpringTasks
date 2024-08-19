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


    public List<Course> showRecommendedCourses() {
        List<Course> courses = courseRecommender.recommendedCourses();
        courses.forEach(course -> System.out.println(course));
        return courseRepository.findAll().stream().limit(5).collect(Collectors.toList());
    }


    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public void updateCourse(Course course) {
        if (courseRepository.existsById(course.getId())) {
            courseRepository.save(course);
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



    public Page<CourseDTO> viewAllCoursesPaginated(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(courseMapper::toCourseDTO);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

}

