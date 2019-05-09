package services;

import models.PetriNet;
import models.QualityMeasure;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.dataawarecnetminer.converter.CausalNetToPetrinet;
import org.processmining.dataawarecnetminer.exception.MiningException;
import org.processmining.dataawarecnetminer.mining.classic.HeuristicsCausalGraphMiner;
import org.processmining.dataawarecnetminer.mining.classic.HeuristicsCausalNetMiner;
import org.processmining.dataawarecnetminer.model.DependencyAwareCausalGraph;
import org.processmining.dataawarecnetminer.model.FrequencyAwareCausalNet;
import org.processmining.datapetrinets.DataPetriNet;
import org.processmining.log.csv.CSVFile;
import org.processmining.log.csv.CSVFileReferenceOpenCSVImpl;
import org.processmining.log.csv.config.CSVConfig;
import org.processmining.log.csvimport.CSVConversion;
import org.processmining.log.csvimport.config.CSVConversionConfig;
import org.processmining.models.cnet.CausalNet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMf;
import org.processmining.plugins.InductiveMiner.plugins.IM;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithILP;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.etm.model.narytree.NAryTree;
import org.processmining.plugins.etm.model.narytree.TreeUtils;
import org.processmining.plugins.etm.parameters.ETMParam;
import org.processmining.plugins.etm.parameters.ETMParamFactory;
import org.processmining.plugins.etm.ui.plugins.ETMwithoutGUI;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.pnalignanalysis.conformance.AlignmentPrecGen;
import org.processmining.plugins.pnalignanalysis.conformance.AlignmentPrecGenRes;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.conversion.ProcessTree2Petrinet;
import utils.DiscoveryAlgorithm;
import utils.FakeProMContext;

import java.io.File;
import java.util.*;


public class ProMWrapper {

    public XLog convertCsvToXes(String csvPath) {
        try {
            File csv = new File(csvPath);
            CSVFile csvFile = new CSVFileReferenceOpenCSVImpl(csv.toPath());
            CSVConfig importConfig = new CSVConfig(csvFile);
            CSVConversionConfig conversionConfig = new CSVConversionConfig(csvFile, importConfig);
            conversionConfig.autoDetect();
            conversionConfig.setCaseColumns(Collections.singletonList("traceid"));
            conversionConfig.setEventNameColumns(Collections.singletonList("event"));
            conversionConfig.setCompletionTimeColumn("timestamp");
            CSVConversion conversion = new CSVConversion();
            CSVConversion.ConversionResult<XLog> result = conversion.doConvertCSVToXES(csvFile, importConfig, conversionConfig);
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public PetriNet mine(XLog log, DiscoveryAlgorithm algorithm) throws Exception {
        PetriNet petriNet = null;
        switch (algorithm) {
            case HeuristicMiner:
                petriNet = mineUsingHeuristicMiner(log);
                break;
            case InductiveMiner:
                petriNet = mineUsingInductiveMiner(log);
                break;
            case EvolutionaryTreeMiner:
                petriNet = mineUsingETM(log);
                break;
        }

        return petriNet;
    }

    public QualityMeasure getQualityMeasure(XLog log, PetriNet petriNet) {
        FakeProMContext context = new FakeProMContext();

        try {
            Map<Transition, Integer> costMOS = constructMOSCostFunction(petriNet.net);
            XEventClassifier eventClassifier = MiningParameters.getDefaultClassifier();
            Map<XEventClass, Integer> costMOT = constructMOTCostFunction(petriNet.net, log, eventClassifier);
            TransEvClassMapping mapping = constructMapping(petriNet.net, log, eventClassifier);

            CostBasedCompleteParam parameters = new CostBasedCompleteParam(costMOT, costMOS);
            if (petriNet.initialMarking != null)
                parameters.setInitialMarking(petriNet.initialMarking);
            if (petriNet.finalMarking != null)
                parameters.setFinalMarkings(petriNet.finalMarking);
            parameters.setGUIMode(false);
            parameters.setCreateConn(false);
            parameters.setNumThreads(8);
            parameters.setMaxNumOfStates(37 * 1000);

            PNLogReplayer r = new PNLogReplayer();
            PNRepResult result = r.replayLog(context, petriNet.net, log, mapping, new PetrinetReplayerWithILP(), parameters);
            Map<String, Object> replayInfo = result.getInfo();
            AlignmentPrecGen app = new AlignmentPrecGen();
            AlignmentPrecGenRes xa = app.measureConformanceAssumingCorrectAlignment(context, mapping, result, petriNet.net, petriNet.initialMarking, true);

            QualityMeasure measure = new QualityMeasure();
            measure.fitness = (double)replayInfo.getOrDefault("Trace Fitness", 0);
            measure.generalization = xa.getGeneralization();
            measure.precision = xa.getPrecision();
            return measure;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PetriNet mineUsingInductiveMiner(XLog log) {
        Object[] net = IM.minePetriNet(new FakeProMContext(), log, new MiningParametersIMf());
        PetrinetImpl a = (PetrinetImpl) net[0];
        return new PetriNet(net);
    }

    private PetriNet mineUsingHeuristicMiner(XLog log) throws MiningException {
        HeuristicsCausalGraphMiner gminer = new HeuristicsCausalGraphMiner(log, new XEventNameClassifier());
        DependencyAwareCausalGraph w = gminer.mineCausalGraph();
        HeuristicsCausalNetMiner miner = new HeuristicsCausalNetMiner(log, new XEventNameClassifier());
        FrequencyAwareCausalNet net = miner.mineCausalNet(w);
        CausalNet result = net.getCNet();
        CausalNetToPetrinet conv = new CausalNetToPetrinet();
        DataPetriNet.PetrinetWithMarkings e = conv.convert(result);
        return new PetriNet(e);
    }

    private PetriNet mineUsingETM(XLog log) throws Exception {
        FakeProMContext context = new FakeProMContext();
        ETMParam param = ETMParamFactory.buildStandardParam(log, null);
        param.getCentralRegistry().getRandom().setSeed(1);
        NAryTree[] trees = new NAryTree[] { TreeUtils.fromString("LEAF: tau") };
        param.setSeed(trees);
        param.addTerminationConditionMaxDuration(1000 * 60 * 1);
        param.setLogModulo(1);
        ProcessTree tree = ETMwithoutGUI.minePTWithParameters(context, log, MiningParameters.getDefaultClassifier(), param);
        ProcessTree2Petrinet con = new ProcessTree2Petrinet();
        Object[] net = con.convert(context, tree);
        return new PetriNet(net);
    }

    private static Map<Transition, Integer> constructMOSCostFunction(PetrinetGraph net) {
        Map<Transition, Integer> costMOS = new HashMap<>();

        for (Transition t : net.getTransitions())
            if (t.isInvisible())
                costMOS.put(t, 0);
            else
                costMOS.put(t, 1);

        return costMOS;
    }

    private static Map<XEventClass, Integer> constructMOTCostFunction(PetrinetGraph net, XLog log, XEventClassifier eventClassifier) {
        Map<XEventClass, Integer> costMOT = new HashMap<XEventClass, Integer>();
        XLogInfo summary = XLogInfoFactory.createLogInfo(log, eventClassifier);

        for (XEventClass evClass : summary.getEventClasses().getClasses()) {
            costMOT.put(evClass, 1);
        }

        return costMOT;
    }

    private static TransEvClassMapping constructMapping(PetrinetGraph net, XLog log, XEventClassifier eventClassifier) {
        TransEvClassMapping mapping = new TransEvClassMapping(eventClassifier, new XEventClass("DUMMY", 99999));
        XLogInfo summary = XLogInfoFactory.createLogInfo(log, eventClassifier);
        for (Transition t : net.getTransitions()) {
            for (XEventClass evClass : summary.getEventClasses().getClasses()) {
                String id = evClass.getId();

                if (t.getLabel().equals(id)) {
                    mapping.put(t, evClass);
                    break;
                }
            }

        }

        return mapping;
    }

}
