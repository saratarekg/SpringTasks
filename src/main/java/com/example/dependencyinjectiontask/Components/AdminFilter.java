package com.example.dependencyinjectiontask.Components;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, ServletException {
        String uri = request.getRequestURI();

        if (uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
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