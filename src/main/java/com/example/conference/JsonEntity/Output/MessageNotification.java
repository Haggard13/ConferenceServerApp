package com.example.conference.JsonEntity.Output;

public class MessageNotification {
    public MessageNotification(String name, String text, int id) {
        this.name = name;
        this.text = text;
        this.id = id;
    }

    public MessageNotification() {
    }

    public String name;
    public String text;
    public int id;
}
