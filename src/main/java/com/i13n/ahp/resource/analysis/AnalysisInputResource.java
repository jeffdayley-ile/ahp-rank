package com.i13n.ahp.resource.analysis;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class AnalysisInputResource {
    @NotNull
    private String name;
    @NotNull
    private List<String> criteria;
    @NotNull
    private List<String> options;
    @NotNull
    private Map<String, List<String>> optionsCriterion;

    public AnalysisInputResource() {
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, List<String>> getOptionsCriterion() {
        return optionsCriterion;
    }

    public void setOptionsCriterion(Map<String, List<String>> optionsCriterion) {
        this.optionsCriterion = optionsCriterion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
