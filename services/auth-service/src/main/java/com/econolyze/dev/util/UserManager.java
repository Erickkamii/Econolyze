package com.econolyze.dev.util;

import com.econolyze.dev.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.transaction.Transactional;

public class UserManager {

    final static String USERROLE = "USER";
    final static String ADMINROLE = "ADMIN";
    final static String SEPARATOR = ",";

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
            // Não precisa chamar persist() se dentro de transação e objeto está gerenciado
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

    public static String generateJWT(String username) {
        User user = User.find("username", username).firstResult();
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return TokenGenerator.generate(username, user.getRoles());
    }
}
