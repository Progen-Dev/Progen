package de.mtorials.fortnite.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
class ModelStatistics {

    private int data;

    int getSoloPlacetop1() {

        return this.data;
    }
}
