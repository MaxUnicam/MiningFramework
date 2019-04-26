package services.interfaces;

import models.PetriNet;
import models.QualityMeasure;
import org.deckfour.xes.model.XLog;


public interface IResultHandler {

    boolean createResultsDir(String contract);

    boolean saveLog(XLog log, String contract);

    boolean savePetrinet(PetriNet net, String contract);

    boolean saveMeasures(QualityMeasure measures, String contract);

}
