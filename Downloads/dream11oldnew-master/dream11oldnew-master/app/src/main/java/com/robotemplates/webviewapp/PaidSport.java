package com.robotemplates.webviewapp;

public class PaidSport {
    private int id;
    private int sportid;
    private String testemonials;

    public String getPaidsportname() {
        return paidsportname;
    }

    public void setPaidsportname(String paidsportname) {
        this.paidsportname = paidsportname;
    }

    private String paidsportname;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSportid() {
        return sportid;
    }

    public void setSportid(int sportid) {
        this.sportid = sportid;
    }

    public String getTestemonials() {
        return testemonials;
    }

    public void setTestemonials(String testemonials) {
        this.testemonials = testemonials;
    }
}
