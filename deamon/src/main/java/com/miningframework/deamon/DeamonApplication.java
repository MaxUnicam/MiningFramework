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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class DeamonApplication {

    public static void main(String [] args) {

        System.out.println("Mining framework deamon started");
        ApplicationSettings settings = ApplicationSettings.instance();
        settings.initialize();
        System.out.println("Application settings initialized");
        IResultHandler resultHandler = new ResultsHandler();
        ProMWrapper prom = new ProMWrapper();
        LogBuilder logBuilder = new LogBuilder(settings.contractIndexUri);
        System.out.println("Log builder created");

        if (logBuilder.contractHashes == null) {
            System.out.println("Empty list of contracts");
            return;
        }

        final List<DiscoveryAlgorithm> algoritms = Arrays.asList(
                DiscoveryAlgorithm.HeuristicMiner,
                DiscoveryAlgorithm.InductiveMiner
        );

        ExecutionController controller = new ExecutionController(prom);

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
                resultHandler.createMeasuresFile(contract);

                for (DiscoveryAlgorithm algorithm : algoritms) {
                    try {
                        PetriNet petrinet = controller.mine(log, algorithm);
                        resultHandler.savePetrinet(petrinet, contract, algorithm);
                        System.out.println("Model discovered with " + algorithm.name());
                        QualityMeasure measure = controller.getQualityMeasure(log, petrinet);
                        resultHandler.saveMeasures(measure, contract, algorithm);
                        if (measure != null) {
                            System.out.println(algorithm.name() + " model quality measures");
                            System.out.println(measure.toString());
                        }
                    } catch (Exception e) {
                        if (e instanceof TimeoutException)
                            System.out.println("Timeout, skipped");
                        else
                            e.printStackTrace();
                    }
                }
            }

            System.out.println("----------------------------------------------------");
        }

        controller.deinitialize();
    }

}