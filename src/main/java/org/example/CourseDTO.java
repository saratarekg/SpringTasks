package org.example;

public class CourseDTO {
    private String title;
    private String description;
    private int credit;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(int id, String title, String description, int credit) {
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }


    @Override
    public String toString() {
        return "CourseDTO{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", credit=" + credit +
                '}';
    }
}

