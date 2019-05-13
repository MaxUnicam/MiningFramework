package com.miningframework.common.settings;

import com.google.gson.Gson;

import java.io.*;
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
        String fileContent = new String(getFileData());
        Gson gson = new Gson();
        ApplicationSettings temporary = gson.fromJson(fileContent, ApplicationSettings.class);

        this.etherscanApiKey = temporary.etherscanApiKey;
        this.contractIndexUri = temporary.contractIndexUri;
        this.outputLocation = temporary.outputLocation;
        this.minimumActiveUsersNumber = temporary.minimumActiveUsersNumber;
        this.minimumTransactionNumber = temporary.minimumTransactionNumber;
        this.minimumMethodNumber = temporary.minimumMethodNumber;
    }

    private byte[] getFileData() {
        try {
            URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
            String path = url.getPath().substring(0, url.getPath().lastIndexOf("/"));
            File file = new File(path + "/appsettings.json");

            // Loaded from properties during development and from the same directory of the jar in production
            InputStream inputStream;
            if (file.exists())
                inputStream = new FileInputStream(file);
            else
                inputStream = this.getClass().getResourceAsStream("/settings/appsettings.json");

            if (inputStream != null) {
                byte[] data = toByteArray(inputStream);
                inputStream.close();
                return data;
            }

            System.out.println("Appsettings.json not found");
            return new byte[0];
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }

}
