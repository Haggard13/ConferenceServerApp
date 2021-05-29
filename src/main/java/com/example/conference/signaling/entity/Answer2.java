package com.example.conference.signaling.entity;

public class Answer2 {
    private final String id;
    private final int userId;
    private final String answer;

    public Answer2(String id, int userId, String answer) {
        this.id = id;
        this.userId = userId;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getAnswer() {
        return answer;
    }
}
