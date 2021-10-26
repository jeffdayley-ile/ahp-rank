package com.i13n.AHP.resource;

public class OptionResource {
    final private String name;
    final private Integer rank;
    final private Double score;
    final private Criterion criterion;

    public OptionResource(String name, Integer rank, Double score, Criterion criterion) {
        this.name = name;
        this.rank = rank;
        this.score = score;
        this.criterion = criterion;
    }

    public String getName() {
        return name;
    }

    public Integer getRank() {
        return rank;
    }

    public Double getScore() {
        return score;
    }

    public Criterion getCriterion() {
        return criterion;
    }
}
