package com.example.conference.sqlQuery;

import com.example.conference.JsonEntity.Input.ConferenceMessage;

public class CMessageQueries {
    public static String addMessageQuery(ConferenceMessage cm, int type) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.messages_conference VALUES(" +
                    "'" + cm.text + "', " +
                    cm.conference_id + ", " +
                    "'" + cm.date_time + "', " +
                    cm.sender_id + ", '" +
                    cm.sender_name + "', '" +
                    cm.sender_surname + "', " +
                    type + ")" +
                    "SELECT MAX(id) FROM dbo.messages_conference;" +
                "COMMIT;";
    }

    public static String maxMessageIdQuery(int conference_id, int user_id) {
        return "USE conference;" +
                "SELECT MAX(id) " +
                    "FROM dbo.messages_conference " +
                    "WHERE conference_id = " + conference_id +
                        " AND sender_id != " + user_id + ";";
    }

    public static String lastMessageIdQuery(int conference_id , int user_id) {
        return "USE conference;" +
                "SELECT last_message_id " +
                    "FROM dbo.user_conferences " +
                    "WHERE id_conference = " + conference_id +
                        " AND id_user = " + user_id + ";";
    }

    public static String messageTextQuery(int message_id) {
        return "USE conference;" +
                "SELECT messages_text " +
                    "FROM dbo.messages_conference " +
                    "WHERE id = " + message_id + ";";
    }

    public static String conferenceIDAndNameQuery(int conference_id) {
        return "USE conference;" +
                "SELECT id, name " +
                    "FROM dbo.conferences " +
                    "WHERE id = " + conference_id + ";";
    }

    public static String updateLastMessageIDQuery(int last_message_id, int conference_id, int user_id) {
        return "USE conference; " +
                "UPDATE dbo.user_conferences " +
                    "SET last_message_id = " + last_message_id +
                    " WHERE id_conference =" + conference_id +
                        " AND id_user = " + user_id + ";";
    }

    public static String getNewMessagesQuery(int conference_id, int l_m_id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM messages_conference " +
                    "WHERE (conference_id = " + conference_id +
                        " AND id > " + l_m_id + ");";
    }
}
