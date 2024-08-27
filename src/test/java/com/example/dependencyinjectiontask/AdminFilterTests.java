package com.example.dependencyinjectiontask;


import com.example.dependencyinjectiontask.Components.AdminFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class AdminFilterTest {

    private AdminFilter adminFilter;

    @BeforeEach
    void setUp() {
        adminFilter = new AdminFilter();
    }

    @Test
    void testDoFilterWithNoAuthorizationHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        adminFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Access denied", response.getErrorMessage());
    }

    @Test
    void testDoFilterWithInvalidAuthorizationHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "InvalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        adminFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals("Access denied", response.getErrorMessage());
    }

    @Test
    void testDoFilterWithValidAuthorizationHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Admin");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        adminFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
