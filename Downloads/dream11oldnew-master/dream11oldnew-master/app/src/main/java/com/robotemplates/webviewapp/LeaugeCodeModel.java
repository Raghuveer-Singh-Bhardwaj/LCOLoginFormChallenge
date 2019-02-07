package com.robotemplates.webviewapp;

public class LeaugeCodeModel {
    private String matchName;
    private String winningAmount;
    private String contestSize;
    private String id;
    private String entryFee;
    private int winner;
    private String multipleteams;
    private String contestCode;

  /*  public int compareTo(LeaugeCodeModel o) {
        if (this.getId().compareTo(o.getId()) > 1) {
            return toString().compareTo(o.getId());
        } else if (this.getId().compareTo(o.getId()) < 1) {
            return toString().compareTo(o.getId());
        }
        return 0;
    }*/
    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(String winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getContestSize() {
        return contestSize;
    }

    public void setContestSize(String contestSize) {
        this.contestSize = contestSize;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getContestCode() {
        return contestCode;
    }

    public void setContestCode(String contestCode) {
        this.contestCode = contestCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getMultipleteams() {
        return multipleteams;
    }

    public void setMultipleteams(String multipleteams) {
        this.multipleteams = multipleteams;
    }
}
