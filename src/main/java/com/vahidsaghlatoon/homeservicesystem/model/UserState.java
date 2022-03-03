package com.vahidsaghlatoon.homeservicesystem.model;

public enum UserState {
    NEW(1, "جدید"),
    AWAITING_APPROVAL(2, "در انتظار تایید"),
    CONFIRMED(3, "تایید شده");

    private String stateMessage;
    private int id;

    UserState(int id, String stateMessage) {
        this.stateMessage = stateMessage;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
