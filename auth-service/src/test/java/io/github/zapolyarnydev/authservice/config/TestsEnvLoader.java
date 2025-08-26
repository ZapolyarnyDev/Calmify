package io.github.zapolyarnydev.authservice.config;

import io.github.cdimascio.dotenv.Dotenv;

public class TestsEnvLoader {

    public static void load() {
        var dotenv = Dotenv.configure()
                .filename(".tests.env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach( dotenvEntry ->
                System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
        );
    }
}
