package com.i13n.ahp.resource.analysis;

import java.util.UUID;

public class AnalysisResource {
    private UUID id;
    private String name;

    public AnalysisResource() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
