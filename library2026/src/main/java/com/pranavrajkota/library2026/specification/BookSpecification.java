package com.pranavrajkota.library2026.specification;

import com.pranavrajkota.library2026.entity.Book;
import com.pranavrajkota.library2026.entity.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasAuthor(String authorName) {
        return (root, query, cb) ->
                cb.equal(
                        cb.lower(root.get("author").get("name")),
                        authorName.toLowerCase()
                );
    }

    public static Specification<Book> hasCategory(String categoryName) {

        return (root, query, cb) -> {

            query.distinct(true);
            Join<Book, Category> categoryJoin = root.join("categories");

            return cb.equal(
                    cb.lower(categoryJoin.get("name")),
                    categoryName.toLowerCase()
            );
        };
    }

    public static Specification<Book> hasGenre(String genre) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("genreOfBook").as(String.class),
                        genre
                );
    }
}