package com.example.dependencyinjectiontask;

import javax.persistence.*;
import org.example.Author;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorServiceTest {
    @MockBean
    private AuthorRepository authorRepository;

    @InjectMocks
    @Autowired
    private AuthorService authorService;

    @Test
    void getAuthor_AuthorExists_returnAuthor() {
        Date birthdate = new Date(23/6/2002);
        Author author = new Author("sara","sara@sumerge.com",birthdate);
        String email= "sara@sumerge.com";

        when(authorRepository.findByEmail(email)).thenReturn(author);

        Author result = authorService.getAuthorByEmail(email);

        assertEquals(author.getName(), result.getName());
        assertEquals(author.getEmail(), result.getEmail());

    }

    @Test
    void getAuthor_authorNotExists_returnAuthor() {
        String email= "sara@sumerge.com";

        when(authorRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorByEmail(email);
        });

        verify(authorRepository).findByEmail(email);


    }

}