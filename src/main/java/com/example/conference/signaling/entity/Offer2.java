package com.example.conference.signaling.entity;

public class Offer2 {
    private final String id;
    private final int userId;
    private final String offer;

    public Offer2(String id, int userId, String offer) {
        this.id = id;
        this.userId = userId;
        this.offer = offer;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getOffer() {
        return offer;
    }
}
