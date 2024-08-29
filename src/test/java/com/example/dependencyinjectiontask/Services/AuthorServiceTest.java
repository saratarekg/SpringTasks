package com.example.dependencyinjectiontask.Services;

import com.example.dependencyinjectiontask.Mappers.AuthorMapper;
import com.example.dependencyinjectiontask.Repositories.AuthorRepository;
import org.example.Author;
import org.example.AuthorDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    public AuthorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuthor_AuthorExists_returnAuthorDTO() {
        Date birthdate = new Date(23/6/2002);
        Author author = new Author("Sara", "sara@sumerge.com", birthdate);
        AuthorDTO authorDTO = new AuthorDTO("Sara", "sara@sumerge.com");
        String email = "sara@sumerge.com";

        when(authorRepository.findByEmail(email)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.getAuthorByEmail(email);

        assertEquals(authorDTO.getName(), result.getName());
        assertEquals(authorDTO.getEmail(), result.getEmail());
    }

    @Test
    void getAuthor_authorNotExists_throwsEntityNotFoundException() {
        String email = "sara@sumerge.com";

        when(authorRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorByEmail(email);
        });

        verify(authorRepository).findByEmail(email);
    }

    @Test
    void getAuthor_invalidEmailFormat_throwsIllegalArgumentException() {
        String invalidEmail = "invalid-email";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            authorService.getAuthorByEmail(invalidEmail);
        });

        assertEquals("Invalid email format.", thrown.getMessage());
    }
}
