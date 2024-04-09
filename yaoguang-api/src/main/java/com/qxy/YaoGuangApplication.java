package com.qxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class YaoGuangApplication {

    public static void main(String[] args) {

        SpringApplication.run(YaoGuangApplication.class, args);

    }

}
