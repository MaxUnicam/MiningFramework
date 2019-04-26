package services;

import models.PetriNet;
import models.QualityMeasure;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlGZIPSerializer;
import org.processmining.plugins.pnml.exporting.PnmlExportNetToPNML;
import services.interfaces.IResultHandler;
import settings.ApplicationSettings;
import utils.FakeProMContext;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ResultsHandler implements IResultHandler {

    private String outputLocation;

    private String generalMeasuresCsvFile;


    public ResultsHandler() {
        outputLocation = ApplicationSettings.instance().outputLocation;
        generalMeasuresCsvFile = outputLocation + "measures.csv";
        createDir(outputLocation);
        createGeneralCsvMeasuresFile();
    }


    @Override
    public boolean createResultsDir(String contract) {
        String path = outputLocation + contract;
        return createDir(path);
    }

    @Override
    public boolean saveLog(XLog log, String contract) {
        try {
            String path = outputLocation + contract + "/log.xes.gz";
            File outputFile = new File(path);
            new XesXmlGZIPSerializer().serialize(log, new FileOutputStream(outputFile));
            return outputFile.exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean savePetrinet(PetriNet net, String contract) {
        try {
            String path = outputLocation + contract + "/inductive.pnml";
            PnmlExportNetToPNML exporter = new PnmlExportNetToPNML();
            File outputFile = new File(path);
            exporter.exportPetriNetToPNMLFile(new FakeProMContext(), net.net, outputFile);
            return outputFile.exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean saveMeasures(QualityMeasure measures, String contract) {
        String path = outputLocation + contract + "/measures.csv";

        List<String> data = Arrays.asList(Double.toString(measures.fitness),
                Double.toString(measures.precision),
                Double.toString(measures.generalization));
        String dataRow = String.join(",", data);

        String content = "fitness,precision,generalization" + System.lineSeparator() + dataRow;
        boolean value1 = writeFile(path, content);

        String generalMeasureRow = contract + "," + dataRow;
        boolean value2 = appendToCsvMeasuresFile(generalMeasureRow);

        return value1 && value2;
    }


    private void createGeneralCsvMeasuresFile() {
        String content = "contract,fitness,precision,generalization";
        writeFile(generalMeasuresCsvFile, content);
    }

    private boolean appendToCsvMeasuresFile(String row) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(generalMeasuresCsvFile, true));
            writer.newLine();
            writer.write(row);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createDir(String path) {
        return (new File(path)).mkdirs();
    }

    private boolean dirExist(String path) {
        File dir = new File(path);
        return dir.exists();
    }

    private boolean writeFile(String path, String content) {
        try {
            File file = new File(path);
            FileWriter fr = new FileWriter(file);
            fr.write(content);
            fr.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
