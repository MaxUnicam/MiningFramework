package com.miningframework.common.models;

import java.util.List;
import java.util.Map;


public class AnalysisItem {

    public String contract;

    public Map<String, Integer> methodInvocations;

    public Map<String, List<String>> methodCallers;

}
