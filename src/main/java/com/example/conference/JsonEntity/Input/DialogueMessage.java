package com.example.conference.JsonEntity.Input;

public class DialogueMessage {
    public String text;
    public int dialogue_id;
    public String date_time;
    public int sender_id;
    public String sender_name;
    public String sender_surname;

    public DialogueMessage(String text,
                           int dialogue_id,
                           String date_time,
                           int sender_id,
                           String sender_name,
                           String sender_surname) {
        this.text = text;
        this.dialogue_id = dialogue_id;
        this.date_time = date_time;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.sender_surname = sender_surname;
    }
}
