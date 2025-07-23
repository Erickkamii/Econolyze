package com.econolyze.dev.util;

import com.econolyze.dev.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.transaction.Transactional;

public class UserManager {

    final static String USERROLE = "USER";
    final static String ADMINROLE = "ADMIN";
    final static String SEPARATOR = ",";

    @Transactional
    public static void addUser(User user){
        if(!userExists(user.username)){
            user.roles = USERROLE;
            user.password = BcryptUtil.bcryptHash(user.password);
            user.persist();
        }else {

        }
    }

    @Transactional
    public static void addAdmin(Long userId){
        User user = User.findById(userId);
        if(userExists(user.username)){
            addRole(user, ADMINROLE);
            user.persist();
        }
    }



    private static boolean userExists(String username) {
        return (User.count("username", username) > 0);
    }

    private static void addRole(User user, String role){
        user.roles = user.roles + SEPARATOR + role;
    }
    public static String generateJWT(String username){
        User user = User.find("username", username).firstResult();
        return TokenGenerator.generate(username, user.roles);
    }
}
