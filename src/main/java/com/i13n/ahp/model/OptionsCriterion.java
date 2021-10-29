package com.i13n.ahp.model;

import java.util.UUID;

public class OptionsCriterion {
    private UUID id;
    private UUID analysisId;
    private UUID criterionId;
    private UUID optionsId;
    private Integer rank;
    private float score;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(UUID analysisId) {
        this.analysisId = analysisId;
    }

    public UUID getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(UUID criterionId) {
        this.criterionId = criterionId;
    }

    public UUID getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(UUID optionsId) {
        this.optionsId = optionsId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
