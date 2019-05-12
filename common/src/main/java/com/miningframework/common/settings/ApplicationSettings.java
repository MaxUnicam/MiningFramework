package com.miningframework.common.settings;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ApplicationSettings {

    private static ApplicationSettings instance;

    private boolean initialized = false;


    public String contractIndexUri;

    public String etherscanApiKey;

    public String outputLocation;

    public int minimumTransactionNumber = 10;

    public int minimumMethodNumber = 3;

    public int minimumActiveUsersNumber = 2;


    private ApplicationSettings() { }

    public static ApplicationSettings instance() {
        if (instance == null) {
            instance = new ApplicationSettings();
        }

        return instance;
    }


    public void initialize() {
        if (initialized) {
            return;
        }

        initialized = true;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL resource = classLoader.getResource("settings/appsettings.json");
        String settingsFileUri = (resource != null) ? resource.getFile() : "";

        byte[] data = getFileData(settingsFileUri);
        String fileContent = new String(data);

        Gson gson = new Gson();
        ApplicationSettings temporary = gson.fromJson(fileContent, ApplicationSettings.class);

        this.etherscanApiKey = temporary.etherscanApiKey;
        this.contractIndexUri = temporary.contractIndexUri;
        this.outputLocation = temporary.outputLocation;
        this.minimumActiveUsersNumber = temporary.minimumActiveUsersNumber;
        this.minimumTransactionNumber = temporary.minimumTransactionNumber;
        this.minimumMethodNumber = temporary.minimumMethodNumber;
    }

    private byte[] getFileData(String uri) {
        try {
            File file = new File(uri);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int size = fis.read(data);
            fis.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
