package services;

import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import services.interfaces.IJsonParser;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JsonParser implements IJsonParser {

    private static final String NONCE_FIELD = "nonce";
    private static final String BLOCK_HASH_FIELD = "blockHash";
    private static final String BLOCK_NUMBER_FIELD = "blockNumber";
    private static final String FROM_FIELD = "from";
    private static final String TO_FIELD = "to";
    private static final String INPUT_FIELD = "input";
    private static final String TIMESTAMP_FIELD = "timeStamp";

    private static final String NAME_FIELD = "name";
    private static final String TYPE_FIELD = "type";
    private static final String STATE_FIELD = "stateMutability";
    private static final String CONSTANT_FIELD = "constant";
    private static final String PAYABLE_FIELD = "payable";
    private static final String INPUTS_FIELD = "inputs";
    private static final String OUTPUTS_FIELD = "outputs";

    @Override
    public List<Transaction> parseTransactions(String jsonString) {
        try {
            JSONObject etherscanResult = new JSONObject(StringUtils.normalizeJson(jsonString));
            if (!etherscanResult.has("result"))
                return null;

            List<Transaction> transactions = new ArrayList<>();
            JSONArray json = etherscanResult.getJSONArray("result");
            for (Object item : json) {
                Transaction transaction = parseTransaction((JSONObject) item);
                if (transaction != null)
                    transactions.add(transaction);
            }
            return transactions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Transaction parseTransaction(String jsonString) {
        try {
            JSONObject json = new JSONObject(StringUtils.normalizeJson(jsonString));
            return parseTransaction(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MethodABI> parseMethodABI(String json) {
        try {
            JSONObject etherscanResult = new JSONObject(StringUtils.normalizeJson(json));
            if (!etherscanResult.has("result"))
                return null;

            String serializedArray = etherscanResult.getString("result");
            JSONArray result = new JSONArray(serializedArray);
            List<MethodABI> abis = new ArrayList<>();
            for (Object item : result) {
                JSONObject obj = (JSONObject) item;
                MethodABI abi = parseMethodAbi(obj);
                if (abi != null) {
                    abis.add(abi);
                }
            }

            return abis;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Transaction parseTransaction(JSONObject json) {
        Transaction transaction = new Transaction();
        if (json.has(NONCE_FIELD))
            transaction.nonce = json.getInt(NONCE_FIELD);
        if (json.has(BLOCK_HASH_FIELD))
            transaction.blockHash = json.getString(BLOCK_HASH_FIELD);
        if (json.has(BLOCK_NUMBER_FIELD))
            transaction.blockNumber = json.getInt(BLOCK_NUMBER_FIELD);
        if (json.has(FROM_FIELD))
            transaction.from = json.getString(FROM_FIELD);
        if (json.has(TO_FIELD))
            transaction.to = json.getString(TO_FIELD);
        if (json.has(INPUT_FIELD))
            transaction.input = json.getString(INPUT_FIELD);
        if (json.has(TIMESTAMP_FIELD))
            transaction.timestamp = json.getLong(TIMESTAMP_FIELD);
        return transaction;
    }

    private MethodABI parseMethodAbi(JSONObject json) {
        MethodABI abi = new MethodABI();
        if (json.has(NAME_FIELD))
            abi.name = json.getString(NAME_FIELD);
        if (json.has(TYPE_FIELD))
            abi.type = json.getString(TYPE_FIELD);
        if (json.has(STATE_FIELD))
            abi.stateMutability = json.getString(STATE_FIELD);
        if (json.has(CONSTANT_FIELD))
            abi.constant = json.getBoolean(CONSTANT_FIELD);
        if (json.has(PAYABLE_FIELD))
            abi.payable = json.getBoolean(PAYABLE_FIELD);

        if (json.has(INPUTS_FIELD)) {
            JSONArray inputs = json.getJSONArray(INPUTS_FIELD);
            if (inputs != null && inputs.length() > 0) {
                abi.inputs = new ArrayList<>();
                for (Object item : inputs) {
                    JSONObject itemJson = (JSONObject)item;
                    MethodABIParameter param = new MethodABIParameter();
                    param.name = itemJson.getString("name");
                    param.type = itemJson.getString("type");
                    abi.inputs.add(param);
                }
            }
        }

        if (json.has(OUTPUTS_FIELD)) {
            JSONArray outputs = json.getJSONArray(OUTPUTS_FIELD);
            if (outputs != null && outputs.length() > 0) {
                abi.outputs = new ArrayList<>();
                for (Object item : outputs) {
                    JSONObject itemJson = (JSONObject) item;
                    MethodABIParameter param = new MethodABIParameter();
                    param.name = itemJson.getString("name");
                    param.type = itemJson.getString("type");
                    abi.outputs.add(param);
                }
            }
        }

        return abi;
    }
}