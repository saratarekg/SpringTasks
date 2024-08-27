package com.example.dependencyinjectiontask.Mappers;

import org.example.Author;
import org.example.AuthorDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel ="spring")
public interface AuthorMapper {
    AuthorDTO toAuthorDTO(Author author);
    Author toAuthor(AuthorDTO authorDTO);
}
