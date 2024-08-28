package com.example.dependencyinjectiontask.Repositories;

import org.example.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pageable);


}
