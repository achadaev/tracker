package com.example.tracker.shared.model;

import java.util.List;

public class SelectionValue {
    private List<ProcedureType> types;
    private List<User> users;

    public SelectionValue() {
    }

    public SelectionValue(List<ProcedureType> types, List<User> users) {
        this.types = types;
        this.users = users;
    }

    public List<ProcedureType> getTypes() {
        return types;
    }

    public void setTypes(List<ProcedureType> types) {
        this.types = types;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
