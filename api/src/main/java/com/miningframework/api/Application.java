package com.miningframework.api;

import com.miningframework.common.settings.ApplicationSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationSettings settings = ApplicationSettings.instance();
        settings.initialize();
        SpringApplication.run(Application.class, args);
    }
}