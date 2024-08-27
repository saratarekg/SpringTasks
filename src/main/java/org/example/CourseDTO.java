package org.example;

public class CourseDTO {
    private String title;
    private String description;
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

