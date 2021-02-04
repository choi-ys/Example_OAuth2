package io.example.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        log.info("AuthorizationServerApplication is Running");
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
