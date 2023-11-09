package com.pshs.attendancesystem.entities;

public class WebSocketData {

    private String mode;
    private String hashedLrn;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getHashedLrn() {
        return hashedLrn;
    }

    public void setHashedLrn(String hashedLrn) {
        this.hashedLrn = hashedLrn;
    }
}
