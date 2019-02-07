package com.robotemplates.webviewapp;

import java.util.ArrayList;

public class User {
    private String mId="0";
    private int mRefferals;
    private String mPhone;
    private ArrayList<String> mListOfRefferers;
    private boolean withdraw;
    private String name;
    private String emailId;
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public int getmRefferals() {
        return mRefferals;
    }

    public void setmRefferals(int mRefferals) {
        this.mRefferals = mRefferals;
    }

    public ArrayList<String> getmListOfRefferers() {
        return mListOfRefferers;
    }

    public void setmListOfRefferers(ArrayList<String> mListOfRefferers) {
        this.mListOfRefferers = mListOfRefferers;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public boolean isWithdraw() {
        return withdraw;
    }

    public void setWithdraw(boolean withdraw) {
        this.withdraw = withdraw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
