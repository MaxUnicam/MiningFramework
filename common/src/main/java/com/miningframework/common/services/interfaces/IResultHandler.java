package com.miningframework.common.services.interfaces;

import com.miningframework.common.models.PetriNet;
import com.miningframework.common.models.QualityMeasure;
import com.miningframework.common.utils.DiscoveryAlgorithm;
import org.deckfour.xes.model.XLog;


public interface IResultHandler {

    boolean createResultsDir(String contract);

    boolean saveLog(XLog log, String contract);

    boolean savePetrinet(PetriNet net, String contract, DiscoveryAlgorithm algorithm);

    boolean createMeasuresFile(String contract);

    boolean saveMeasures(QualityMeasure measures, String contract, DiscoveryAlgorithm algorithm);

}
