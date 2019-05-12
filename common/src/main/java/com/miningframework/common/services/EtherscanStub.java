package com.miningframework.common.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miningframework.common.models.MethodABI;
import com.miningframework.common.models.Transaction;
import okhttp3.*;
import com.miningframework.common.settings.ApplicationSettings;

import java.io.IOException;
import java.util.List;


class EtherscanStub {

    private class EhterscanResult<T> {
        String status;
        String message;
        T result;
    }

    private String apiKey;

    private OkHttpClient httpClient;

    private Gson gson;


    EtherscanStub() {
        ApplicationSettings settings = ApplicationSettings.instance();
        apiKey = settings.etherscanApiKey;
        httpClient = new OkHttpClient();
        gson = new Gson();
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

        String body = response.body().string();
        EtherscanStub.EhterscanResult<List<Transaction>> result =
                gson.fromJson(body, new TypeToken<EtherscanStub.EhterscanResult<List<Transaction>>>(){}.getType());
        return result.result;
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

        String body = response.body().string();
        EtherscanStub.EhterscanResult<String> result =
                gson.fromJson(body, new TypeToken<EtherscanStub.EhterscanResult<String>>(){}.getType());
        return gson.fromJson(result.result, new TypeToken<List<MethodABI>>(){}.getType());
    }

}