package com.econolyze.dev.service;

import com.econolyze.dev.dto.LoginResponseDTO;
import com.econolyze.dev.model.User;
import com.econolyze.dev.util.TokenGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class UserManager {

    static final String USERROLE = "USER";
    static final String ADMINROLE = "ADMIN";
    static final String SEPARATOR = ",";

    private final Request request;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserManager(Request request) {
        this.request = request;
    }

    @Transactional
    public static void addUser(User user) {
        if (!userExists(user.getUsername())) {
            user.setRoles(USERROLE);
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
            addRole(user, ADMINROLE);
        }
    }

    private static boolean userExists(String username) {
        return (User.count("username", username) > 0);
    }

    private static void addRole(User user, String role) {
        String roles = user.getRoles();
        if (roles == null || roles.isBlank()) {
            user.setRoles(role);
        } else if (!roles.contains(role)) {
            user.setRoles(roles + SEPARATOR + role);
        }
    }

    public static LoginResponseDTO login(String username, String rawPassword) {
        User user = User.find("username", username).firstResult();

        if (user == null || !BcryptUtil.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUserId(user.id);
        loginResponseDTO.setUsername(username);
        loginResponseDTO.setRefreshToken(TokenGenerator.generateRefreshToken(username));
        loginResponseDTO.setAuthToken(TokenGenerator.generate(user.getUsername(), user.getRoles()));
        return loginResponseDTO;
    }

    public LoginResponseDTO refreshTokenFromJWT() {
        try {
            // Verificar se é refresh token
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

            LoginResponseDTO response = new LoginResponseDTO();
            response.setUsername(user.getUsername());
            response.setUserId(user.id);
            response.setAuthToken(TokenGenerator.generate(user.getUsername(), user.getRoles()));
            response.setRefreshToken(TokenGenerator.generateRefreshToken(user.getUsername()));

            return response;
        } catch (Exception e) {
            System.err.println("Erro no refresh: " + e.getMessage());
            throw new WebApplicationException("Falha ao validar token", Response.Status.UNAUTHORIZED);
        }
    }
}
