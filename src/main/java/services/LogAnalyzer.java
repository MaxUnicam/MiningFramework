package services;

import models.AnalysisItem;
import models.Transaction;
import services.interfaces.ILogAnalyzer;
import settings.ApplicationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogAnalyzer implements ILogAnalyzer {

    @Override
    public AnalysisItem AnalyzeContract(String contract, List<Transaction> contractTransactions) {
        AnalysisItem analysis = new AnalysisItem();
        analysis.contract = contract;
        analysis.methodInvocations = new HashMap<>();
        analysis.methodCallers = new HashMap<>();
        for (Transaction transaction : contractTransactions) {
            if (transaction.input == null || transaction.input.length() <= 10)
                continue;

            String method = transaction.input.substring(0, 10);
            Integer currentMethodCardinality = analysis.methodInvocations.get(method);
            analysis.methodInvocations.put(method, currentMethodCardinality != null ? currentMethodCardinality + 1 : 1);

            List<String> callers = analysis.methodCallers.get(method);
            if (callers == null) {
                callers = new ArrayList<>();
            }

            if (!callers.contains(transaction.from))
                callers.add(transaction.from);
            analysis.methodCallers.put(method, callers);
        }

        return analysis;
    }

    @Override
    public boolean isContractIntresting(AnalysisItem analysisItem) {
        if (analysisItem == null) {
            return false;
        }

        ApplicationSettings settings = ApplicationSettings.instance();
        if (analysisItem.methodInvocations.size() < settings.minimumMethodNumber) {
            return false;
        }

        int total = 0;
        int activeCallers = 0;
        for (String key : analysisItem.methodInvocations.keySet()) {
            total += analysisItem.methodInvocations.get(key);
            List<String> methodCallers = analysisItem.methodCallers.get(key);
            if (methodCallers != null && methodCallers.size() > settings.minimumActiveUsersNumber) {
                activeCallers ++;
            }
        }

        if (total < settings.minimumTransactionNumber) {
            return false;
        }

        //TODO: Controllare
//        if (activeCallers < 2) {
//            return false;
//        }

        return true;
    }

}
