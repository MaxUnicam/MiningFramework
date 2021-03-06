package com.miningframework.common.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miningframework.common.models.AnalysisItem;
import com.miningframework.common.models.MethodABI;
import com.miningframework.common.models.Transaction;
import com.miningframework.common.services.interfaces.ILogAnalyzer;
import com.miningframework.common.settings.ApplicationSettings;
import com.miningframework.common.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LogBuilder {

    public List<String> contractHashes = new ArrayList<>();

    private EtherscanStub etherscan = new EtherscanStub();

    private ILogAnalyzer analyzer = new LogAnalyzer();

    private LogFormatter logFormatter = new LogFormatter();

    private String temporaryDirPath;


    public LogBuilder(String contractsIndexFileUri) {
        temporaryDirPath = ApplicationSettings.instance().outputLocation + "tmp/";
        new File(temporaryDirPath).mkdirs();
        byte[] data = getFileData(contractsIndexFileUri);
        String content = new String(data);
        Gson gson = new Gson();
        contractHashes = gson.fromJson(content, new TypeToken<List<String>>(){}.getType());
    }

    public LogBuilder() {
        temporaryDirPath = ApplicationSettings.instance().outputLocation + "tmp/";
        new File(temporaryDirPath).mkdirs();
    }


    public String build(String contract) {
        if (StringUtils.isNullOrEmpty(contract))
            return "";

        try {
            System.out.println(contract + "'s transactions download start");
            List<Transaction> transactions = etherscan.getContractTransactions(contract);
            if (transactions == null) {
                return null;
            }

            System.out.println(contract + "'s transactions downloaded");
            AnalysisItem res = analyzer.AnalyzeContract(contract, transactions);
            if (analyzer.isContractIntresting(res)) {
                Map<String, List<Transaction>> x = transactions.stream().collect(Collectors.groupingBy(t -> t.from));
                System.out.println(contract + "'s method abis download start");
                List<MethodABI> abis = etherscan.getContractABI(contract);
                System.out.println(contract + "'s method abis downloaded");
                String fileContent = logFormatter.format(x, abis);
                System.out.println("Csv log content generated");
                String path = writeFile(fileContent, contract);
                System.out.println("File csv created");
                return path;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    private String writeFile(String content, String fileName) {
        try {
            String path = temporaryDirPath + fileName + ".csv";
            File file = new File(path);
            FileWriter fr = new FileWriter(file);
            fr.write(content);
            fr.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
