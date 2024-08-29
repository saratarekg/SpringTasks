package com.example.dependencyinjectiontask.Mappers;

import org.example.Author;
import org.example.AuthorDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class AuthorMapperTest {

    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void testToAuthorDTO() {
        Date birthdate = new Date(23/6/2002);
        Author author = new Author("Sara", "sara@sumerge.com", birthdate);
        AuthorDTO authorDTO = authorMapper.toAuthorDTO(author);

        assertEquals(author.getName(), authorDTO.getName());
        assertEquals(author.getEmail(), authorDTO.getEmail());

    }

    @Test
    void testToAuthor() {
        AuthorDTO authorDTO = new AuthorDTO("Sara", "sara@sumerge.com");
        Author author = authorMapper.toAuthor(authorDTO);

        assertEquals(authorDTO.getName(), author.getName());
        assertEquals(authorDTO.getEmail(), author.getEmail());

    }
}
