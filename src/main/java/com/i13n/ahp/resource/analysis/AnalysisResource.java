package com.i13n.ahp.resource.analysis;

import java.util.UUID;

public class AnalysisResource {
    private UUID id;

    public AnalysisResource() {
        this.id = UUID.randomUUID();
    }

    public AnalysisResource(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
