package com.vahidsaghlatoon.homeservicesystem.model;

public enum OfferState {
    WAITING_FOR_THE_SELECTION(1, "منتظر انتخاب"),
    SELECTED(2, "انتخاب شده");
    private String stateMessage;
    private int id;

    OfferState(int id, String stateMessage) {
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
