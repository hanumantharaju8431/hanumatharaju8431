package com.bagshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BagShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BagShopApplication.class, args);
    }
}
