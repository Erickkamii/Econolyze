package com.econolyze.dev;

import com.econolyze.dev.model.User;
import com.econolyze.dev.util.UserManager;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;

import static com.econolyze.dev.util.UserManager.addAdmin;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        io.quarkus.runtime.Quarkus.run(args);
    }

    public static class SimpleLogin implements QuarkusApplication{
        @Override
        @ActivateRequestContext
        public int run(String... args) throws Exception {
            if (args.length>0){
                String[] userData = args[0].split(",");
                addAdmin(userData[0], userData[1]);
            }
            Quarkus.waitForExit();
            return 0;
        }

        private void addAdmin(String username, String password){
            User admin = new User();
            admin.username = username;
            admin.password = password;
            UserManager.addUser(admin);
            UserManager.addAdmin(admin.id);
        }
    }
}
