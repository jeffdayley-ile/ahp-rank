package com.i13n.ahp.model;

import java.util.UUID;

public class Analysis {
    private UUID id;

    public Analysis() {

    }

    public Analysis(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
