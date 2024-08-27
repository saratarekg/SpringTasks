package com.example.dependencyinjectiontask.Mappers;

import org.example.Course;
import org.example.CourseDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel ="spring")
public interface CourseMapper {
    CourseDTO toCourseDTO(Course course);
    Course toCourse(CourseDTO courseDTO);
}
