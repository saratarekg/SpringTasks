package com.example.dependencyinjectiontask.Services;

import com.example.dependencyinjectiontask.Mappers.AuthorMapper;
import com.example.dependencyinjectiontask.Repositories.AuthorRepository;
import org.example.Author;
import org.example.AuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    public AuthorDTO getAuthorByEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        Author author = authorRepository.findByEmail(email);
        if (author == null) {
            throw new EntityNotFoundException("Author with email " + email + " not found.");
        }
        return authorMapper.toAuthorDTO(author);
    }
}
