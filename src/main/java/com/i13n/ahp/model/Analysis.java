package com.i13n.ahp.model;

import java.util.UUID;

public class Analysis {
    private UUID id;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
