package com.example.dependencyinjectiontask;

import org.example.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorByEmail(String email) {
        return authorRepository.findByEmail(email);
    }
}

