package com.example.conference.JsonEntity.Input;

import java.util.List;

public class Conference {
    public List<ConferenceMember> members;
    public String name;
    public int count;

    public Conference(List<ConferenceMember> members, String name, int count) {
        this.members = members;
        this.name = name;
        this.count = count;
    }
}
