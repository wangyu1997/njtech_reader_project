package com.reader.demo.reader.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String imgSrc;
    private String description;
    private String contentUrl;

    @ManyToMany(mappedBy = "books")
    private Set<SysUser> users = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "sys_book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();
}
