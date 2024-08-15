package com.example.dependencyinjectiontask;

import com.example.dependencyinjectiontask.CourseRepository;
import org.example.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCourseRepository implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (name, description, credit, author_id) VALUES (?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, course.getTitle(), course.getDescription(), course.getCredit(), course.getAuthorId());
            System.out.println("Course added successfully. Number of rows affected: " + rowsAffected);
        } catch (DataAccessException e) {
            // Log or handle the exception
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    @Override
    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET name = ?, description = ?, credit = ?, author_id = ? WHERE id = ?";

        try {
            jdbcTemplate.update(sql, course.getTitle(), course.getDescription(), course.getCredit(), course.getAuthorId(), course.getId());
        } catch (DataAccessException e) {
            // Handle exception, e.g., log, rethrow
            throw new RuntimeException("Error updating course", e);
        }


    }

    @Override
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

    @Override
    public List<Course> viewAllCourses() {
        String sql = "SELECT * FROM Course";

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
        return courses;
    }


    @Override
    public void deleteCourse(int id) {
        String sql = "DELETE FROM Course WHERE id = ?";
        jdbcTemplate.update(sql, id);
        System.out.println("Course with ID " + id + " deleted successfully.");
    }

    static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course("","",0,0);
            course.setId(rs.getInt("id"));
            course.setTitle(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setCredit(rs.getInt("credit"));
            course.setAuthorId(rs.getInt("author_id"));
            return course;
        }
    }

}
