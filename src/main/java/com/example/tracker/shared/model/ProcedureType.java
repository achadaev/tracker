package com.example.tracker.shared.model;

public class ProcedureType {
    private int id;
    private int kind;
    private String name;

    public ProcedureType(int id, int kind, String name) {
        this.id = id;
        this.kind = kind;
        this.name = name;
    }

    public ProcedureType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
