package services;

import models.MethodABI;
import models.Transaction;
import okhttp3.*;
import services.interfaces.IJsonParser;
import settings.ApplicationSettings;
import utils.StringUtils;

import java.io.IOException;
import java.util.List;


class EtherscanStub {

    private String apiKey;

    private OkHttpClient httpClient;

    private IJsonParser jsonParser;


    EtherscanStub() {
        ApplicationSettings settings = ApplicationSettings.instance();
        apiKey = settings.etherscanApiKey;
        httpClient = new OkHttpClient();
        jsonParser = new JsonParser();
    }


    List<Transaction> getContractTransactions(String contractHash) throws IOException {
        String url = String.format("http://api.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=asc&apikey=%s",
                contractHash,
                apiKey);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        if (response.body() == null) {
            throw new IOException("Empty response body");
        }

        String normalizedJson = StringUtils.normalizeJson(response.body().string());
        return jsonParser.parseTransactions(normalizedJson);
    }


    List<MethodABI> getContractABI(String contract) throws IOException {
        String url = String.format("https://api.etherscan.io/api?module=contract&action=getabi&address=%s&apikey=%s",
                contract,
                apiKey);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        if (response.body() == null) {
            throw new IOException("Empty response body");
        }

        String normalizedJson = StringUtils.normalizeJson(response.body().string());
        return jsonParser.parseMethodABI(normalizedJson);
    }

}