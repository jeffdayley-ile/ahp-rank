package com.i13n.ahp.resource.optionsCriterion;

import java.util.UUID;

public class OptionsCriterionResource {
    private UUID id;
    private UUID analysisId;
    private UUID optionsId;
    private UUID criterionId;
    private Integer rank;
    private float score;

    public OptionsCriterionResource() {
    }

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

    public UUID getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(UUID optionsId) {
        this.optionsId = optionsId;
    }

    public UUID getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(UUID criterionId) {
        this.criterionId = criterionId;
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
