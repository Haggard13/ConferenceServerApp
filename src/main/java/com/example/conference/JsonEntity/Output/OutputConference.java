package com.example.conference.JsonEntity.Output;

public class OutputConference {
    public String name;
    public int count;
    int id;
    int notification;

    public OutputConference(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
        notification = 1;
    }
}
