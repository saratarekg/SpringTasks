package com.example.dependencyinjectiontask;


import com.example.dependencyinjectiontask.Configurations.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;

@SpringBootApplication
@Import(ApplicationConfig.class)
@EntityScan(basePackages = {"org.example"})
@EnableJpaRepositories(basePackages = {"com.example.dependencyinjectiontask"})
public class DependencyInjectionTaskApplication {

    public static void main(String[] args) throws SQLException {


        SpringApplication.run(DependencyInjectionTaskApplication.class, args);


    }

}
