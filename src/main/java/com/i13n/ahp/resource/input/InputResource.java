package com.i13n.ahp.resource.input;

import java.util.List;
import java.util.Map;

public class InputResource {
    private List<String> criteria;
    private List<String> options;
    private Map<String, List<String>> optionsCriterion;

    public InputResource() {
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
}
