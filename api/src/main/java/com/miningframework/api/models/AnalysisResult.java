package com.miningframework.api.models;

import com.miningframework.common.models.QualityMeasure;
import com.miningframework.common.utils.DiscoveryAlgorithm;


public class AnalysisResult {

    public DiscoveryAlgorithm algorithm;

    public QualityMeasure qualityMeasure;

    public String pnmlContent;

    public String xesContent;

}
