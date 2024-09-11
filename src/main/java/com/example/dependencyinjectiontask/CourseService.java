package com.example.dependencyinjectiontask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.List;
import org.example.CourseRecommender;
import org.example.Course;
import org.springframework.transaction.annotation.Transactional;


@Component
public class CourseService {
    private CourseRecommender courseRecommender;

    private JdbcTemplate jdbcTemplate;
    public CourseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void testConnection() {
        String sql = "SELECT 1";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
            System.out.println("Connection test result: " + result);
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Error testing connection: " + e.getMessage());
        }
    }


    @Autowired
    public void setCourseRecommender(@Qualifier("PythonRecommender") CourseRecommender courseRecommender) {
        this.courseRecommender = courseRecommender;
    }


    public void showRecommendedCourses() {
        List<Course> courses = courseRecommender.recommendedCourses();
//        courses.forEach(course -> System.out.println(course));
    }
    List<Course> getRecommendedCourses(){
        return courseRecommender.recommendedCourses();
    }


    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (name, description, credit, author_id) VALUES (?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, course.getTitle(), course.getDescription(), course.getCredit());
            System.out.println("Course added successfully. Number of rows affected: " + rowsAffected);
        } catch (DataAccessException e) {
            // Log or handle the exception
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET name = ?, description = ?, credit = ? WHERE id = ?";

        try {
            jdbcTemplate.update(sql, course.getTitle(), course.getDescription(), course.getCredit(), course.getId());
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no course is found
            System.out.println("Course with ID " + course.getId() + " not found.");
        }

    }
    public Course viewCourse(int id) {
        String sql = "SELECT * FROM Course WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CourseRowMapper());

        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no course is found
            System.out.println("Course with ID " + id + " not found.");
            return null; // or throw a custom exception if needed
        }
    }

    public void viewAllCourses() {
        String sql = "SELECT * FROM Course";
        System.out.println( jdbcTemplate.queryForList(sql));

        // Retrieve all courses
        List<Course> courses = jdbcTemplate.query(sql, new CourseRowMapper());

        // Print all courses
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }


    public void deleteCourse(int id) {
        String sql = "DELETE FROM Course WHERE id = ?";
        jdbcTemplate.update(sql, id);
        System.out.println("Course with ID " + id + " deleted successfully.");
    }


    private static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course("","",0);
            course.setId(rs.getInt("id"));
            course.setTitle(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setCredit(rs.getInt("credit"));
            return course;
        }
    }
}

