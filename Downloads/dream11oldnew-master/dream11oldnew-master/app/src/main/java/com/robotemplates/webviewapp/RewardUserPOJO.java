package com.robotemplates.webviewapp;

import java.util.Date;

public class RewardUserPOJO {
    public String deviceId;
    private String email;
    private String point;
    private Boolean withdraw;

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    private Date lastTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Boolean getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Boolean withdraw) {
        this.withdraw = withdraw;
    }
}
