package com.econolyze.dev.entity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "auth", name = "users")
@UserDefinition
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Username
    @Column(unique = true, nullable = false)
    private String username;
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    @Password
    @Column(nullable = false)
    private String password;
    @Roles
    @Column(nullable = false)
    private String roles;
}