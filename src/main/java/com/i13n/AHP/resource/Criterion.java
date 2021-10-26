package com.i13n.AHP.resource;

public class Criterion {
    final private String name;
    final private Double weight;
    final private Integer rank;

    public Criterion(String name, Double weight, Integer rank) {
        this.name = name;
        this.weight = weight;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getRank() {
        return rank;
    }
}
