package com.i13n.ahp.resource.analysis;

import com.i13n.ahp.resource.criteria.CriteriaResource;
import com.i13n.ahp.resource.options.OptionsResource;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionResource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AnalysisOutputResource {
    private UUID analysisId;
    private List<CriteriaResource> criteria;
    private List<OptionsResource> options;
    private List<OptionsCriterionResource> optionsCriterion;

    public AnalysisOutputResource() {
    }

    public UUID getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(UUID analysisId) {
        this.analysisId = analysisId;
    }

    public List<CriteriaResource> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<CriteriaResource> criteria) {
        this.criteria = criteria;
    }

    public List<OptionsResource> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsResource> options) {
        this.options = options;
    }

    public List<OptionsCriterionResource> getOptionsCriterion() {
        return optionsCriterion;
    }

    public void setOptionsCriterion(List<OptionsCriterionResource> optionsCriterion) {
        this.optionsCriterion = optionsCriterion;
    }
}
