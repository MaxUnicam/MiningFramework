package models;

import org.processmining.datapetrinets.DataPetriNet;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;
import org.processmining.models.semantics.petrinet.Marking;

public class PetriNet {

    public Petrinet net;

    public Marking initialMarking;

    public Marking finalMarking;


    public PetriNet(Object[] data) {
        initFromObjects(data);
    }

    public PetriNet(DataPetriNet.PetrinetWithMarkings pn) {
        this.net = pn.getNet();
        this.initialMarking = pn.getInitialMarking();
        this.finalMarking = (pn.getFinalMarkings().length > 0) ? pn.getFinalMarkings()[0] : null;
    }


    private void initFromObjects(Object[] data) {
        try {
            if (data == null || data.length < 1) {
                return;
            }

            net = (PetrinetImpl) data[0];
            if (data.length > 1) {
                initialMarking = (Marking) data[1];
            }

            if (data.length > 2) {
                finalMarking = (Marking) data[2];
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

}
