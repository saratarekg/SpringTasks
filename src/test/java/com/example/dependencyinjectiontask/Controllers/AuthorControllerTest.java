package com.example.dependencyinjectiontask.Controllers;

import com.example.dependencyinjectiontask.Services.AuthorService;
import org.example.Author;
import org.example.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers= AuthorController.class)
@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void getAuthor_emailExists_returnsAuthor() throws Exception {
        String email= "sara@sumerge.com";

        when(authorService.getAuthorByEmail(email)).thenReturn(any(Author.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(content().json("{\"title\":\"testUpdate\",\"description\":\"test update\"}"));
    }

    @Test
    void getAuthor_emailNotExists_returnsNotFound() throws Exception {
        String email= "sara@sumerge.com";

        when(authorService.getAuthorByEmail(email)).thenThrow(new EntityNotFoundException("Author with email " + email + " not found."));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAuthor_catchesException_returnsInternalServerError() throws Exception {
        String email= "sara@sumerge.com";

        when(authorService.getAuthorByEmail(email)).thenThrow(new RuntimeException("Failed to fetch author."));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}