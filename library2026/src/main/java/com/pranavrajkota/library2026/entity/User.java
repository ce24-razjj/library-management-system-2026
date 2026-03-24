package com.pranavrajkota.library2026.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, name = "user_name")
    @NotNull
    private String userName;

    @Column(nullable = false)
    private String password;

    @NotNull // same as -=> @Column(nullable = false)
    private String role;
}
