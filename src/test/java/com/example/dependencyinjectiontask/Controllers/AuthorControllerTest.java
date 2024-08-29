package com.example.dependencyinjectiontask.Controllers;

import com.example.dependencyinjectiontask.GlobalExceptionHandler;
import com.example.dependencyinjectiontask.Services.AuthorService;
import org.example.AuthorDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers= AuthorController.class)
@ContextConfiguration(classes = {AuthorController.class, GlobalExceptionHandler.class})

class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    String email= "sara@sumerge.com";

    @Test
    void getAuthor_emailExists_returnsAuthor() throws Exception {

        when(authorService.getAuthorByEmail(email)).thenReturn(any(AuthorDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(content().json("{\"title\":\"testUpdate\",\"description\":\"test update\"}"));
    }

    @Test
    void getAuthor_emailNotExists_returnsNotFound() throws Exception {

        when(authorService.getAuthorByEmail(email)).thenThrow(new EntityNotFoundException("Author with email " + email + " not found."));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAuthor_catchesException_returnsInternalServerError() throws Exception {

        when(authorService.getAuthorByEmail(email)).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/email")
                        .param("email", "sara@sumerge.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred. Please try again later."));

    }

}