package services.interfaces;

import models.*;

import java.util.List;

public interface IJsonParser {

    List<Transaction> parseTransactions(String json);

    Transaction parseTransaction(String json);

    List<MethodABI> parseMethodABI(String json);

}
