package models;

public class QualityMeasure {

    public double fitness;

    public double precision;

    public double generalization;


    public String toString() {
        return "Fitness: " + fitness + System.lineSeparator() +
               "Precision: " + precision + System.lineSeparator() +
               "Generalization: " + generalization;
    }

}
