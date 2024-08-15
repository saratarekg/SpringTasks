package com.example.dependencyinjectiontask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.example.CourseRecommender;
import org.example.PythonCourseRecommender;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.example.dependencyinjectiontask")
@ComponentScan("org.example")
public class ApplicationConfig {
    @Bean
    public DataSource dataSource() throws SQLException {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/JDBCTask"); // Update with your MySQL DB details
        dataSource.setUsername("root");
        dataSource.setPassword("SE@maadi2024");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("schema.sql"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "JavaRecommender")
    public CourseRecommender javaRecommender(JdbcTemplate jdbcTemplate) {
        return new JavaCourseRecommender(jdbcTemplate);
    }

    @Bean(name="PythonRecommender")
    public CourseRecommender pythonRecommender() {
        return new PythonCourseRecommender();
    }







}

