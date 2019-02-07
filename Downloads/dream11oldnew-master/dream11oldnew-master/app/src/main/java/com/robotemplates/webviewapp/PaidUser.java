package com.robotemplates.webviewapp;

public class PaidUser {
    private int id;
    private String email;

    public String getPaidsportname() {
        return paidsportname;
    }

    public void setPaidsportname(String paidsportname) {
        this.paidsportname = paidsportname;
    }

    private String paidsportname;
    private String password;
    private int  paidsportid;

    public String getStartdate() {
        return startdate;
    }


    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    private String startdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPaidsportid() {
        return paidsportid;
    }

    public void setPaidsportid(int paidsportid) {
        this.paidsportid = paidsportid;
    }
}
