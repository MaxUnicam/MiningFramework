import models.PetriNet;
import models.QualityMeasure;
import org.deckfour.xes.model.XLog;
import services.LogBuilder;
import services.ProMWrapper;
import services.ResultsHandler;
import services.interfaces.IResultHandler;
import settings.ApplicationSettings;
import utils.DiscoveryAlgorithm;

public class Program {

    public static void main(String [] args) {
        System.out.println("Application start");
        ApplicationSettings settings = ApplicationSettings.instance();
        settings.initialize();
        System.out.println("Application settings initialized");
        IResultHandler resultHandler = new ResultsHandler();
        ProMWrapper prom = new ProMWrapper();
        LogBuilder logBuilder = new LogBuilder(settings.contractsIndexFileUrl);
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