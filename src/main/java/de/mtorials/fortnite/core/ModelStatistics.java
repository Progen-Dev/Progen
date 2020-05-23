package de.mtorials.fortnite.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties()
class ModelStatistics {

    private int data;

    int getSoloPlacetop1() {

        return this.data;
    }
}
