package com.example.conference.signaling.entity;

public class LeavingUser {
    private final String id;
    private final int userId;

    public LeavingUser(String id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }
}
