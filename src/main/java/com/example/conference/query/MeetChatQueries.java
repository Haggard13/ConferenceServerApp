package com.example.conference.query;

import com.example.conference.json.ConferenceMessageWithoutID;

import java.text.SimpleDateFormat;

public class MeetChatQueries {

    public static String sendMessage(ConferenceMessageWithoutID cm) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                "INSERT dbo.messages_meet VALUES(" +
                "N'" + cm.getText() + "', " +
                cm.getConference_id() + ", " +
                "'" + new SimpleDateFormat("yyyy-MM-dd").format(cm.getDate_time()) +
                "T" +
                new SimpleDateFormat("HH:mm:ss.SSS").format(cm.getDate_time()) + "', " +
                cm.getSender_id() + ", N'" +
                cm.getSender_name() + "', N'" +
                cm.getSender_surname() + "', " +
                cm.getType() + ")" +
                "SELECT MAX(id) FROM dbo.messages_meet;" +
                "COMMIT;";
    }

    public static String checkNewMessages(int conferenceID, int lastMessageID) {
        return "USE conference;" +
                "SELECT COUNT(*) FROM dbo.messages_meet " +
                "WHERE id > " + lastMessageID + " AND conference_id = " + conferenceID;
    }

    public static String getNewMessages(int conferenceID, int lastMessageID) {
        return "USE conference;" +
                "SELECT * FROM dbo.messages_meet " +
                "WHERE id > " + lastMessageID + " AND conference_id = " + conferenceID;
    }
}
