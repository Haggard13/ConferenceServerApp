package com.example.conference.query;

import com.example.conference.json.CMessageEntity;

import java.text.SimpleDateFormat;

public class CMessageQueries {
    public static String addMessageQuery(CMessageEntity cm) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.messages_conference VALUES(" +
                    "N'" + cm.getText() + "', " +
                    cm.getConference_id() + ", " +
                    "'" + new SimpleDateFormat("yyyy-MM-dd").format(cm.getDate_time()) +
                    "T" +
                    new SimpleDateFormat("HH:mm:ss.SSS").format(cm.getDate_time()) + "', " +
                    cm.getSender_id() + ", N'" +
                    cm.getSender_name() + "', N'" +
                    cm.getSender_surname() + "', " +
                    cm.getType() + ")" +
                    "SELECT MAX(id) FROM dbo.messages_conference;" +
                "COMMIT;";
    }

    public static String getNewMessagesQuery(int conferenceID, int lastMessageID) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM messages_conference " +
                    "WHERE (conference_id = " + conferenceID +
                        " AND id > " + lastMessageID + ");";
    }

    public static String checkNewMessages(int conferenceID, int lastMessageID) {
        return "USE conference;" +
                "SELECT COUNT(*) FROM dbo.messages_conference " +
                "WHERE id > " + lastMessageID + " AND conference_id = " + conferenceID;
    }
}
