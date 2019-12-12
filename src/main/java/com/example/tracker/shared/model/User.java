package com.example.tracker.shared.model;

public class User {
    private int id;
    private String login;
    private String password;
    private String regDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRegDate() { return regDate; }

    public void setRegDate(String regDate) { this.regDate = regDate; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
