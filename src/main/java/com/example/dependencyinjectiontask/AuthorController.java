package com.example.dependencyinjectiontask;

import org.example.Author;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Author> getAuthorByEmail(@RequestParam String email) {
        Author author = authorService.getAuthorByEmail(email);
        return ResponseEntity.ok(author);
    }

}
