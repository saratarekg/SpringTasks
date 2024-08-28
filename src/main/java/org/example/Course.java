package org.example;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String title;
    private String description;
    private Integer credit;


    @ManyToMany(mappedBy = "courses")
    private Set<Author> authors;

    @OneToMany(mappedBy = "course")
    private Set<Rating> ratings;

    @OneToOne(mappedBy = "course")
    private Assessment assessment;


    public Course(String title, String description, Integer credit) {
        this.title = title;
        this.description = description;
        this.credit = credit;
    }
    public Course(int id, String title, String description, Integer credit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.credit = credit;
    }

    public Course() {

    }

    public long getId() { return id; }
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

    public Integer getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }



    @Override
    public String toString() {
        return "Course{ id= "+id+", name='" + title + "', description='" + description + "'}";
    }


}
