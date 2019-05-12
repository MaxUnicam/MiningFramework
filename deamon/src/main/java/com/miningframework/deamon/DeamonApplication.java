package com.miningframework.deamon;

import com.miningframework.common.models.PetriNet;
import com.miningframework.common.models.QualityMeasure;
import com.miningframework.common.services.LogBuilder;
import com.miningframework.common.services.ProMWrapper;
import com.miningframework.common.services.ResultsHandler;
import com.miningframework.common.services.interfaces.IResultHandler;
import com.miningframework.common.settings.ApplicationSettings;
import com.miningframework.common.utils.DiscoveryAlgorithm;
import org.deckfour.xes.model.XLog;

public class DeamonApplication {

    public static void main(String [] args) throws Exception {

        System.out.println("Mining framework deamon started");
        ApplicationSettings settings = ApplicationSettings.instance();
        settings.initialize();
        System.out.println("Application settings initialized");
        IResultHandler resultHandler = new ResultsHandler();
        ProMWrapper prom = new ProMWrapper();
        LogBuilder logBuilder = new LogBuilder(settings.contractIndexUri);
        System.out.println("Log builder created");

        if (logBuilder.contractHashes != null) {
            for (String contract : logBuilder.contractHashes) {
                String filePath = logBuilder.build(contract);
                System.out.println("Log built correctly: " + (filePath != null) + " from contract " + contract);

                if (filePath != null) {
                    resultHandler.createResultsDir(contract);

                    XLog log = prom.convertCsvToXes(filePath);
                    if (log == null)
                        continue;

                    resultHandler.saveLog(log, contract);
                    System.out.println("Xes log generated");

//                    PetriNet heuristicNet = prom.mine(log, DiscoveryAlgorithm.HeuristicMiner);
//                    resultHandler.savePetrinet(heuristicNet, "tmp");
//                    resultHandler.saveLog(log, "tmp");
//                    System.out.println("Model discovered with heuristic miner");
//                    QualityMeasure heuristicMeasure = prom.getQualityMeasure(log, heuristicNet);
//                    if (heuristicMeasure != null) {
//                        System.out.println("Heuristic miner model quality measures");
//                        System.out.println(heuristicMeasure.toString());
//                    }

                    PetriNet inductiveNet = prom.mine(log, DiscoveryAlgorithm.InductiveMiner);
                    resultHandler.savePetrinet(inductiveNet, contract);
                    System.out.println("Model discovered with inductive miner");

                    QualityMeasure inductiveMeasure = prom.getQualityMeasure(log, inductiveNet);
                    resultHandler.saveMeasures(inductiveMeasure, contract);
                    if (inductiveMeasure != null) {
                        System.out.println("Inductive miner model quality measures");
                        System.out.println(inductiveMeasure.toString());
                    }
                }

                System.out.println("----------------------------------------------------");
            }
        }
    }

}