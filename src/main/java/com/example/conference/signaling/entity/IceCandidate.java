package com.example.conference.signaling.entity;

public class IceCandidate {
    private final String id;
    private final int conferenceId;
    private final int userId;
    private final int senderId;
    private final String iceCandidate;

    public IceCandidate(String id, int conferenceId, int userId, int senderId, String iceCandidate) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.userId = userId;
        this.senderId = senderId;
        this.iceCandidate = iceCandidate;
    }

    public String getId() {
        return id;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public int getUserId() {
        return userId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getIceCandidate() {
        return iceCandidate;
    }
}
