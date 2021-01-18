package com.example.conference.JsonEntity.Output;

public class DMessage {
    int id;
    String text;
    long date_time;
    int sender_id;
    int dialogue_id;
    String sender_name;
    String sender_surname;
    int sender_enum;
    int type;

    public DMessage(int id,
                    String text,
                    int dialogue_id,
                    long date_time,
                    int sender_id,
                    String sender_name,
                    String sender_surname,
                    int sender_enum,
                    int type) {
        this.id = id;
        this.text = text;
        this.date_time = date_time;
        this.sender_id = sender_id;
        this.dialogue_id = dialogue_id;
        this.sender_name = sender_name;
        this.sender_surname = sender_surname;
        this.sender_enum = sender_enum;
        this.type = type;
    }
}
