package com.example.conference.JsonEntity;

public class Dialogue {
    public int first_user_id;
    public int second_user_id;
    public String first_user_email;
    public String second_user_email;
    public String first_user_name;
    public String second_user_name;
    public String first_user_surname;
    public String second_user_surname;

    public Dialogue(int first_user_id,
                    int second_user_id,
                    String first_user_email,
                    String second_user_email,
                    String first_user_name,
                    String second_user_name,
                    String first_user_surname,
                    String second_user_surname) {
        this.first_user_id = first_user_id;
        this.second_user_id = second_user_id;
        this.first_user_email = first_user_email;
        this.second_user_email = second_user_email;
        this.first_user_name = first_user_name;
        this.second_user_name = second_user_name;
        this.first_user_surname = first_user_surname;
        this.second_user_surname = second_user_surname;
    }
}
