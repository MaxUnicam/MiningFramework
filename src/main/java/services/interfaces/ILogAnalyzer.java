package services.interfaces;

import models.AnalysisItem;
import models.Transaction;

import java.util.List;


public interface ILogAnalyzer {

    AnalysisItem AnalyzeContract(String contract, List<Transaction> contractTransactions);

    boolean isContractIntresting(AnalysisItem analysisItem);

}
