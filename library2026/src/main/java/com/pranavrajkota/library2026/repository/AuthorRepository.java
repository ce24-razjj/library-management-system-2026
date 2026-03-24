package com.pranavrajkota.library2026.repository;

import com.pranavrajkota.library2026.entity.Author;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByNameIgnoreCase(String name);
}
