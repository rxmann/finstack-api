package com.app.budget_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BudgetAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetAppApplication.class, args);
    }

}
