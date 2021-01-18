package com.example.conference.JsonEntity.Output;

public class ContactEntityWithStatus {
    String email;
    String name;
    String surname;
    int status;

    public ContactEntityWithStatus(String email, String name, String surname, int status) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.status = status;
    }
}
