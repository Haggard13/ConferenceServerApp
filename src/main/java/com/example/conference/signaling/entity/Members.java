package com.example.conference.signaling.entity;

import java.util.List;

public class Members {
    private final String id;
    private final List<Integer> members;

    public Members(String id, List<Integer> members) {
        this.id = id;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getMembers() {
        return members;
    }
}
