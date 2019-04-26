package models;

import java.math.BigDecimal;

public class Transaction  {

    public int blockNumber;

    public long timestamp;

    public String hash;

    public int nonce;

    public String blockHash;

    public int index;

    public String from;

    public String to;

    public BigDecimal value;

    public long gas;

    public long gasPrice;

    public String input;

}