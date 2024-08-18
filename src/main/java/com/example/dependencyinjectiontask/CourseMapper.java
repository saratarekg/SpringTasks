package com.example.dependencyinjectiontask;

import org.example.Course;
import org.example.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper(componentModel ="spring")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
    CourseDTO toCourseDTO(Course course);
    Course toCourse(CourseDTO courseDTO);
}
