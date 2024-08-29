package com.example.dependencyinjectiontask.Services;

import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import com.example.dependencyinjectiontask.Repositories.CourseRepository;
import org.example.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
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

    public CourseDTO viewCourse(long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toCourseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + id + " not found"));
    }

    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toCourse(courseDTO);
        courseRepository.save(course);
        return courseMapper.toCourseDTO(course);
    }


    public CourseDTO updateCourse(Long id, CourseDTO courseUpdateDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));

        if (!Objects.equals(courseUpdateDTO.getTitle(), "string")) {
            course.setTitle(courseUpdateDTO.getTitle());
        }
        if (!Objects.equals(courseUpdateDTO.getDescription(), "string")) {
            course.setDescription(courseUpdateDTO.getDescription());
        }
        course.setCredit(courseUpdateDTO.getCredit());


        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toCourseDTO(updatedCourse);
    }


    public void deleteCourse(long id) {
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






