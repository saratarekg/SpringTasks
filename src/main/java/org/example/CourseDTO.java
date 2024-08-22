package org.example;

public class CourseDTO {
    private String title;
    private String description;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(String title, String description) {
        this.title = title;
        this.description = description;
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




    @Override
    public String toString() {
        return "CourseDTO{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

