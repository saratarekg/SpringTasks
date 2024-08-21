package com.example.dependencyinjectiontask;

import jakarta.persistence.EntityNotFoundException;
import org.example.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            Author author = authorService.getAuthorByEmail(email);
            return ResponseEntity.ok(author);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch author.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
