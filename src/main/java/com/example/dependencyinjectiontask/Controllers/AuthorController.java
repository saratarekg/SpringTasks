package com.example.dependencyinjectiontask.Controllers;

import com.example.dependencyinjectiontask.Services.AuthorService;
import org.example.AuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("email")
    public ResponseEntity<Object> getAuthorByEmail(@RequestParam String email) {
            AuthorDTO author = authorService.getAuthorByEmail(email);
            return ResponseEntity.ok(author);

    }
}
