package com.example.conference.signaling.entity;

public class IceCandidate2 {
    private final String id;
    private final int userId;
    private final String iceCandidate;

    public IceCandidate2(String id, int userId, String iceCandidate) {
        this.id = id;
        this.userId = userId;
        this.iceCandidate = iceCandidate;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getIceCandidate() {
        return iceCandidate;
    }
}
