package com.example.conference.json.Output;

public class OutputDialogue {
    public int id;
    public int second_user_id;
    public String second_user_name;
    public String second_user_surname;

    public OutputDialogue(int id, int second_user_id, String second_user_name, String second_user_surname) {
        this.id = id;
        this.second_user_id = second_user_id;
        this.second_user_name = second_user_name;
        this.second_user_surname = second_user_surname;
    }
}
