package com.example.conference.signaling;

import java.util.ArrayList;
import java.util.List;

public class VideoConference {
    private final int id;
    private final List<VideoConferenceMember> members;

    public VideoConference(int id) {
        this.id = id;
        this.members = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<VideoConferenceMember> getMembers() {
        return members;
    }

    public void addMember(VideoConferenceMember member) {
        members.add(member);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VideoConference))
            return false;
        VideoConference vc = (VideoConference) obj;
        return vc.getId() == id;
    }
}
