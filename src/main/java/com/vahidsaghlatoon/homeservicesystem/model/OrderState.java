package com.vahidsaghlatoon.homeservicesystem.model;

public enum OrderState {
    WAITING_FOR_THE_OFFER(1, "منتظر پیشنهاد"),
    WAITING_FOR_THE_SELECTION(2, "منتظر انتخاب"),
    WAITING_TO_COME(3, "منتظر آمدن به محل"),
    STARTED(4, "شروع شده"),
    DONE(5, "انجام شده"),
    PAID(6, "پرداخت شده");

    private String stateMessage;
    private int id;

    OrderState(int id, String stateMessage) {
        this.stateMessage = stateMessage;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getStateMessage() {
        return stateMessage;
    }
}
