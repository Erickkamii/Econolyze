package com.econolyze.dev;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;


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
            }
            Quarkus.waitForExit();
            return 0;
        }

    }
}
