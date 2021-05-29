package com.example.conference.signaling.entity;

public class Offer {
    private final String id;
    private final int conferenceId;
    private final int senderId;
    private final int userId;
    private final String offer;

    public Offer(String id, int conferenceId, int senderId, int userId, String offer) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.senderId = senderId;
        this.userId = userId;
        this.offer = offer;
    }

    public String getId() {
        return id;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getOffer() {
        return offer;
    }
}
