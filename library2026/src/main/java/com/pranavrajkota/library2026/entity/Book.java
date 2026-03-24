package com.pranavrajkota.library2026.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

@Setter
@Getter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @NotBlank // -> means only valid string having atleast one char in it is allowed
//    @NotNull -> means empty string as well as strings having only spaces are also acceptable
    private String bookName;

    private int quantity;

    @JsonBackReference
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

//
    // if this line is -nt, then whatever typers og genre are defined in GenreOfBook Enum,
    // will be recorded in data base as numbers, like 0,1, 2, 3, ... // which is actually used in industry to optimise storage
    @Enumerated(EnumType.STRING)
    private GenreOfBook genreOfBook;
// till 13 mar' 26 i have just created this field // will not write it's implementation

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Book(){
    }

    public Book(int id, String bookName, Author author) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
    }

}
