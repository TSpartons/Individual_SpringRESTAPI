package com.sparta.tp.springrest.enums;

public enum Entities {
    ACTOR ("Actor"),
    FILM ("Film");

    private final String entityName;

    public String getEntity(){return this.entityName;}

    Entities(String entityName) {
        this.entityName = entityName;
    }

}
