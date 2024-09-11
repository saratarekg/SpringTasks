package com.example.dependencyinjectiontask;

import javax.persistence.*;
import org.example.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorByEmail(String email) {
        Author author = authorRepository.findByEmail(email);
        if (author == null) {
            throw new EntityNotFoundException("Author with email " + email + " not found.");
        }
        return author;
    }

}

