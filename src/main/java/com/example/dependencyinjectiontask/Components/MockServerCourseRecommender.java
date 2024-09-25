package com.example.dependencyinjectiontask.Components;

import com.example.dependencyinjectiontask.Mappers.CourseMapper;
import org.example.Course;
import org.example.CourseDTO;
import org.example.CourseRecommender;
import org.example.Courses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockServerCourseRecommender implements CourseRecommender {

    private final RestTemplate restTemplate;
    private final CourseMapper courseMapper;

    @Autowired
    public MockServerCourseRecommender(RestTemplate restTemplate, CourseMapper courseMapper) {
        this.restTemplate = restTemplate;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Course> recommendedCourses() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/courses", String.class);
        String xmlResponse = response.getBody();

        List<Course> courses = mapXmlToCourses(xmlResponse);
        return courses;
    }

    private List<Course> mapXmlToCourses(String xmlResponse) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Courses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlResponse);
            Courses coursesXml = (Courses) unmarshaller.unmarshal(reader);

            // Using .getCourse2() to retrieve the list of Course2 objects
            return coursesXml.getCourse2().stream()
                    .map(courseMapper::toCourse)  // Map each Course2 to a Course entity
                    .collect(Collectors.toList());
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XML", e);
        }
    }


}
