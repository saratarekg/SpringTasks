package org.example;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CourseDTO {
    @Valid
    @NotEmpty(message = "Course title cannot be empty")
    private String title;

    private String description;

    @Valid
    @NotNull(message = "Course credit must not be null")
    @Min(value = 1, message = "Course credit must be a positive number")
    private Integer credit;


    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(String title, String description, Integer credit) {
        this.title = title;
        this.description = description;
        this.credit = credit;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }


    @Override
    public String toString() {
        return "CourseDTO{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

