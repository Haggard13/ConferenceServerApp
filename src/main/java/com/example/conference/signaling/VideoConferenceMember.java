package com.example.conference.signaling;

import org.springframework.web.socket.WebSocketSession;

public class VideoConferenceMember {
    private final int id;
    private final WebSocketSession session;
    private final int conferenceId;

    public VideoConferenceMember(int id, WebSocketSession session, int conferenceId) {
        this.id = id;
        this.session = session;
        this.conferenceId = conferenceId;
    }

    public int getId() {
        return id;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VideoConferenceMember))
            return false;
        VideoConferenceMember vcm = (VideoConferenceMember) obj;
        return vcm.session.getId().equals(session.getId()) || vcm.getId() == id;
    }
}
