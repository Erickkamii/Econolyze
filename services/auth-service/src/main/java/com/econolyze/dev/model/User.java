package com.econolyze.dev.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

@Entity
public class User extends PanacheEntity {
    @Id
    public Long id;
    @Column(unique = true)
    @Username
    public String username;
    @Email
    public String email;
    @Password
    public String password;
    @Roles
    public String roles;
}
