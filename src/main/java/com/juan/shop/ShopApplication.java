package com.juan.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author guanhuan_li
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.juan"})
@EnableJpaRepositories(basePackages = {"com.juan"})
@EntityScan(basePackages = {"com.juan"})
@EnableTransactionManagement
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
