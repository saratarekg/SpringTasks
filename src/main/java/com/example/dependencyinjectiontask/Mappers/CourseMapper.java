package com.example.dependencyinjectiontask.Mappers;

import org.example.Course;
import org.example.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel ="spring")
public interface CourseMapper {
    CourseDTO toCourseDTO(Course course);

    @Mapping(target = "id", ignore = true)
    Course toCourse(CourseDTO courseDTO);

}
