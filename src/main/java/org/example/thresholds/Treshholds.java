package org.example.thresholds;

public enum Treshholds {
    MLOC (50),
    CC (25),
    NOP (3),
    LOID (10),
    LOC (500),
    NMO (3),
    NOC (6),
    AVGLOID (8),
    AVGCC (10),
    WMC (15),
    NOM (20),
    DIT (5),
    LCOM (0.6),
    AVGWMC (3),
    AVGNMO (15),
    RMA (0.5),
    DN (0.5),
    RMI (0.5);

    private double i;


    public double getValue(){
        return i;
    }

    Treshholds(double i) {
        this.i = i;
    }
}
