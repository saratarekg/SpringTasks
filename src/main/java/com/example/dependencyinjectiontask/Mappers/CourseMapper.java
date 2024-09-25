package com.example.dependencyinjectiontask.Mappers;
import org.example.Course2;

import org.example.Course;
import org.example.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel ="spring")
public interface CourseMapper {
    CourseDTO toCourseDTO(Course course);

    CourseDTO toCourseDTO(Course2 course2);


    @Mapping(target = "id", ignore = true)
    Course toCourse(Course2 course2);

    @Mapping(target = "id", ignore = true)
    Course toCourse(CourseDTO courseDTO);

}
