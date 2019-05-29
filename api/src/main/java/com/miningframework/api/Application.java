package com.miningframework.api;

import com.miningframework.common.settings.ApplicationSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationHome home = new ApplicationHome(Application.class);
        String settingsFilePath = home.getDir().getAbsolutePath();
        ApplicationSettings settings = ApplicationSettings.instance();
        settings.initialize(settingsFilePath);
        SpringApplication.run(Application.class, args);
    }

}