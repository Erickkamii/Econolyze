package com.econolyze.dev.service;

import com.econolyze.dev.dto.LoginResponseDTO;
import com.econolyze.dev.dto.RegisterRequestDTO;
import com.econolyze.dev.entity.RevokedToken;
import com.econolyze.dev.entity.User;
import com.econolyze.dev.entity.UserRole;
import com.econolyze.dev.repository.RevokedTokenRepository;
import com.econolyze.dev.repository.UserRepository;
import com.econolyze.dev.util.TokenGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserService {

    static final String SEPARATOR = ",";

    @Inject
    TokenGenerator tokenGenerator;
    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;
    @Inject
    RevokedTokenRepository revokedTokenRepository;

    @Transactional
    public User addUser(RegisterRequestDTO dto) {
        if (userExists(dto.username())) {
            throw new WebApplicationException("Username já existe: " + dto.username(), Response.Status.CONFLICT);
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(BcryptUtil.bcryptHash(dto.password()));
        user.setRoles(UserRole.USER.getRole());
        userRepository.persist(user);
        return user;
    }

    public LoginResponseDTO login(String username, String rawPassword) {
        User user = userRepository.find("username", username).firstResult();

        if (user == null || !BcryptUtil.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }
        TokenGenerator.TokenPair accessToken = tokenGenerator.generate(user.getId(), user.getUsername(), user.getRoles());
        TokenGenerator.TokenPair refreshToken = tokenGenerator.generateRefreshToken(user.getId(), user.getUsername());
        return LoginResponseDTO.builder()
                .username(user.getUsername())
                .authToken(accessToken.token())
                .refreshToken(refreshToken.token())
                .build();
    }

    @Transactional
    public LoginResponseDTO refreshTokenFromJWT() {
        String typeClaim = jwt.getClaim("type");
        if(!"refresh".equals(typeClaim)){
            throw new WebApplicationException("Token de refresh inválido", Response.Status.UNAUTHORIZED);
        }
        String jti = jwt.getTokenID();
        if (jti == null || revokedTokenRepository.isRevoked(jti)){
            log.warn("Tentativa de reuso de refresh token revogado. jti={}", jti);
            throw new WebApplicationException("Token inválido ou já utilizado", Response.Status.UNAUTHORIZED);
        }
        String username = jwt.getSubject();
        if (username == null || username.isBlank()){
            throw new WebApplicationException("Token sem subject!", Response.Status.UNAUTHORIZED);
        }

        User user = userRepository.find("username", username).firstResult();
        if (user == null){
            throw new WebApplicationException("Usuário não encontrado: " + username, Response.Status.UNAUTHORIZED);
        }

        RevokedToken revoked = new RevokedToken();
        revoked.jti = jti;
        revoked.expiresAt = jwt.getExpirationTime() > 0
                ? java.time.Instant.ofEpochSecond(jwt.getExpirationTime())
                : java.time.Instant.now().plusSeconds(86400);
        revokedTokenRepository.persist(revoked);

        TokenGenerator.TokenPair accessToken = tokenGenerator.generate(user.getId(), user.getUsername(), user.getRoles());
        TokenGenerator.TokenPair refreshToken = tokenGenerator.generateRefreshToken(user.getId(), user.getUsername());
        return LoginResponseDTO.builder()
                .username(user.getUsername())
                .authToken(accessToken.token())
                .refreshToken(refreshToken.token())
                .build();
    }

    @Transactional
    public void revokeRefreshToken(){
        String type = jwt.getClaim("type");
        if(!"refresh".equals(type)){
            throw new WebApplicationException("Token inválido para logout", Response.Status.UNAUTHORIZED);
        }
        String jti = jwt.getTokenID();
        if (jti != null && !revokedTokenRepository.isRevoked(jti)){
            RevokedToken revoked = new RevokedToken();
            revoked.jti = jti;
            revoked.expiresAt = jwt.getExpirationTime() > 0
                    ? java.time.Instant.ofEpochSecond(jwt.getExpirationTime())
                    : java.time.Instant.now().plusSeconds(86400);
            revokedTokenRepository.persist(revoked);
        }
    }

    private boolean userExists(String username) {
        return (userRepository.count("username", username) > 0);
    }

    @Transactional
    public void addAdmin(Long userId) {
        User user = userRepository.findById(userId);
        if (user != null && userExists(user.getUsername())) {
            addRole(user, UserRole.ADMIN);
        }
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
}
