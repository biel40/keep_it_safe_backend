package com.esliceu.keep_it_safe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KeepItSafeApplication {

    @Autowired
    MockDatabase mockDatabase;

    public static void main(String[] args) {
        SpringApplication.run(KeepItSafeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            mockDatabase.createDatabaseMock();
        };
    }


}
