package com.i13n.AHP.resource;

import java.util.LinkedHashSet;

public class InputResource {

    final private LinkedHashSet<String> ranked_criteria;
    final private LinkedHashSet<String> ranked_options;

    public InputResource(LinkedHashSet<String> ranked_criteria, LinkedHashSet<String> ranked_options) {
        this.ranked_criteria = ranked_criteria;
        this.ranked_options = ranked_options;
    }

    public LinkedHashSet<String> getRanked_criteria() {
        return ranked_criteria;
    }

    public LinkedHashSet<String> getRanked_options() {
        return ranked_options;
    }
}
