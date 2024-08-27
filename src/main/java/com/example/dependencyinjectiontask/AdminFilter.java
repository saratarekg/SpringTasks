package com.example.dependencyinjectiontask;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;



//@Component
public class AdminFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        String uri = request.getRequestURI();
//        System.out.println(uri);
//        if (uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String token = request.getHeader("Authorization");

        System.out.println("inside filter");

        if (token==null ) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied");
            return;
        }

        if (!"Admin".equals(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }


        filterChain.doFilter(request, response);

    }

}