package com.miningframework.common.services.interfaces;

import com.miningframework.common.models.AnalysisItem;
import com.miningframework.common.models.Transaction;

import java.util.List;


public interface ILogAnalyzer {

    AnalysisItem AnalyzeContract(String contract, List<Transaction> contractTransactions);

    boolean isContractIntresting(AnalysisItem analysisItem);

}
