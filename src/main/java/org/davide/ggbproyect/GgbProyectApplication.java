package org.davide.ggbproyect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GgbProyectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GgbProyectApplication.class, args);
    }

}
