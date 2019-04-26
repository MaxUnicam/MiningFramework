package settings;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ApplicationSettings {

    private static ApplicationSettings instance;

    private boolean initialized = false;


    public String contractsIndexFileUrl;

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
        String fileContent = normalizeFileContent(new String(data));
        JSONObject obj = new JSONObject(fileContent);
        contractsIndexFileUrl = obj.getString("ContractIndexUri");
        etherscanApiKey = obj.getString("EtherscanApiKey");
        outputLocation = obj.getString("OutputLocation");

        JSONObject thresholds = obj.getJSONObject("Thresholds");
        minimumMethodNumber = thresholds.getInt("MinimumMethodNumber");
        minimumTransactionNumber = thresholds.getInt("MinimumTransactionNumber");
        minimumActiveUsersNumber = thresholds.getInt("MinimumActiveUsersNumber");
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

    private String normalizeFileContent(String content) {
        return content.replace("\r", "")
                .replace("\n", "")
                .replace("\uFEFF", "")
                .trim();
    }

}
