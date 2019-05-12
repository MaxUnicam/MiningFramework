package com.miningframework.common.models;

import com.miningframework.common.utils.CryptoUtils;

import java.util.List;
import java.util.stream.Collectors;


public class MethodABI
{
    public String name;

    public List<MethodABIParameter> inputs;

    public List<MethodABIParameter> outputs;

    public String type;

    public String stateMutability;

    public boolean constant;

    public boolean payable;

    public String hashedName() {
        if (inputs == null)
            return "";

        String parameters = inputs.stream().map(i -> i.type).collect(Collectors.joining(","));
        String signature = String.format("%s(%s)", name, parameters);
        return String.format("0x%s", CryptoUtils.digestKeccak256(signature));
    }
}
