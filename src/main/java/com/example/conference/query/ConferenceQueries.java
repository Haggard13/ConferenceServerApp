package com.example.conference.query;

import com.example.conference.json.ConferenceEntity;
import com.example.conference.json.Input.ConferenceMember;

import java.text.SimpleDateFormat;

public class ConferenceQueries {
    public static String createConferenceQuery(ConferenceEntity conference) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.conferences " +
                    "VALUES(" +
                        "N'" + conference.getName() + "', " +
                        conference.getCount() + ", " +
                        "N'" + conference.getLast_message() + "', " +
                        "'" + new SimpleDateFormat("yyyy-MM-dd").format(conference.getLast_message_time()) +
                            "T" +
                            new SimpleDateFormat("HH:mm:ss.SSS").format(conference.getLast_message_time()) + "');" +
                    "SELECT MAX(id) FROM dbo.conferences;" +
                "COMMIT;";
    }

    public static String addMemberQuery(ConferenceMember member, int conferenceID) {
        return "USE conference;" +
                "INSERT dbo.user_conferences " +
                    "VALUES(" + member.id + ", " + conferenceID + ", " + member.status + ");";
    }
}
