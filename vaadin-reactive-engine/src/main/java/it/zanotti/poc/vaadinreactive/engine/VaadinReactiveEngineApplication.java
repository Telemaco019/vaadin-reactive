package it.zanotti.poc.vaadinreactive.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories("it.zanotti.poc.vaadinreactive.engine.db.repositories")
@SpringBootApplication
public class VaadinReactiveEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaadinReactiveEngineApplication.class, args);
    }
}
