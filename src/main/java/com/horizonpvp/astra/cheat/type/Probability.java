package com.horizonpvp.astra.cheat.type;

public enum Probability {

    LOW(1),
    MODERATE(2),
    HIGH(3),
    DEFINITE(4);

    public int level;

    Probability(int level) {
        this.level = level;
    }

}
