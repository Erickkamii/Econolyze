package com.econolyze.dev.service;

import com.econolyze.dev.dto.LoginResponseDTO;
import com.econolyze.dev.entity.User;
import com.econolyze.dev.entity.UserRole;
import com.econolyze.dev.util.TokenGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserManager {

    static final String SEPARATOR = ",";

    @Inject
    Request request;

    @Inject
    JsonWebToken jwt;


    @Transactional
    public static void addUser(User user) {
        if (!userExists(user.getUsername())) {
            user.setRoles(UserRole.USER.getRole());
            user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
            user.persist();
        } else {
            throw new IllegalArgumentException("User already exists: " + user.getUsername());
        }
    }

    @Transactional
    public static void addAdmin(Long userId) {
        User user = User.findById(userId);
        if (user != null && userExists(user.getUsername())) {
            addRole(user, UserRole.ADMIN);
        }
    }

    private static boolean userExists(String username) {
        return (User.count("username", username) > 0);
    }

    private static void addRole(User user, UserRole role) {
        String roles = user.getRoles();
        String roleStr = role.getRole();
        if (roles == null || roles.isBlank()) {
            user.setRoles(roleStr);
        } else if (!roles.contains(roleStr)) {
            user.setRoles(roles + SEPARATOR + roleStr);
        }
    }

    public static LoginResponseDTO login(String username, String rawPassword) {
        User user = User.find("username", username).firstResult();

        if (user == null || !BcryptUtil.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }
        return LoginResponseDTO.builder()
                .username(username)
                .refreshToken(TokenGenerator.generateRefreshToken(user.id, username))
                .authToken(TokenGenerator.generate(user.id, user.getUsername(),user.getRoles()))
                .build();
    }

    public LoginResponseDTO refreshTokenFromJWT() {
        try {

            Object typeClaim = jwt.getClaim("type");
            if (!"refresh".equals(typeClaim)) {
                throw new WebApplicationException("Tipo de token inválido", Response.Status.UNAUTHORIZED);
            }

            String username = jwt.getSubject();
            if (username == null || username.isBlank()) {
                throw new WebApplicationException("Token sem subject", Response.Status.UNAUTHORIZED);
            }

            User user = User.find("username", username).firstResult();
            if (user == null) {
                throw new WebApplicationException("Usuário não encontrado", Response.Status.UNAUTHORIZED);
            }

            return LoginResponseDTO.builder()
                    .username(user.getUsername())
                    .authToken(TokenGenerator.generate(user.id, user.getUsername(), user.getRoles()))
                    .refreshToken(TokenGenerator.generateRefreshToken(user.id, user.getUsername()))
                    .build();
        } catch (Exception e) {
            System.err.println("Erro no refresh: " + e.getMessage());
            throw new WebApplicationException("Falha ao validar token", Response.Status.UNAUTHORIZED);
        }
    }
}
