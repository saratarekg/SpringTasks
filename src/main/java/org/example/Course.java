package org.example;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String title;
    private String description;
    private int credit;


    @ManyToMany(mappedBy = "courses")
    private Set<Author> authors;

    public Course(String title, String description, int credit) {
        this.title = title;
        this.description = description;
        this.credit = credit;
    }

    public Course() {

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }



    @Override
    public String toString() {
        return "Course{ id= "+id+", name='" + title + "', description='" + description + "'}";
    }


}
