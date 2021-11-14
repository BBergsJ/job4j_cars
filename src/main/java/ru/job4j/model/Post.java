package ru.job4j.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private Date created;
    private boolean sold;

//    private User user;
//    private Car car;
//    private Set<Image> images = new HashSet<>();
}
