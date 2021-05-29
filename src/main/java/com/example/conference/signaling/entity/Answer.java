package com.example.conference.signaling.entity;

public class Answer {
    private final String id;
    private final int conferenceId;
    private final int answererId;
    private final int userId;
    private final String answer;

    public Answer(String id, int conferenceId, int answererId, int userId, String answer) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.answererId = answererId;
        this.userId = userId;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public int getAnswererId() {
        return answererId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAnswer() {
        return answer;
    }
}
