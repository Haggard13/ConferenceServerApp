package com.example.conference.query;

import com.example.conference.json.DMessageEntity;

import java.text.SimpleDateFormat;

public class DMessageQueries {
    public static String addMessageQuery(DMessageEntity dm) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.messages_dialogue VALUES(N'" +
                    dm.getText() + "', " +
                    dm.getDialogue_id() + ", " +
                    "'" + new SimpleDateFormat("yyyy-MM-dd").format(dm.getDate_time()) +
                    "T" +
                    new SimpleDateFormat("HH:mm:ss.SSS").format(dm.getDate_time()) + "', " +
                    dm.getSender_id() + ", N'" +
                    dm.getSender_name() + "', N'" +
                    dm.getSender_surname() + "', " +
                    dm.getType() + ")" +
                    "SELECT MAX(id) FROM dbo.messages_dialogue;" +
                "COMMIT;";

    }

    public static String maxMessageIdQuery(int dialogue_id, int user_id) {
        return "USE conference;" +
                "SELECT MAX(id) " +
                    "FROM dbo.messages_dialogue " +
                    "WHERE dialogues_id = " + dialogue_id +
                        " AND sender_id != " + user_id + ";";
    }

    public static String lastMessageIdQuery(int dialogue_id, int user_id) {
        return "USE conference;" +
                "SELECT last_message_id " +
                    "FROM dbo.user_dialogues " +
                    "WHERE id_dialogue = " + dialogue_id +
                        " AND user_id = " + user_id + ";";
    }

    public static String messageQuery(int message_id) {
        return "USE conference;" +
                "SELECT messages_text, " +
                    "dialogues_id, " +
                    "sender_name, " +
                    "sender_surname " +
                    "FROM dbo.messages_dialogue " +
                    "WHERE id = " + message_id + ";";
    }

    public static String updateLastMessageIDQuery(int last_message_id, int dialogue_id, int user_id) {
        return "USE conference; " +
                "UPDATE dbo.user_dialogues " +
                    "SET last_message_id = " + last_message_id +
                    " WHERE id_dialogue = " + dialogue_id +
                        " AND user_id = " + user_id + ";";
    }

    public static String getNewMessageQuery(int dialogue_id, int last_message_id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM messages_dialogue " +
                    "WHERE (dialogues_id = " + dialogue_id +
                        " AND id > " + last_message_id + ");";
    }

    public static String checkNewMessages(int dialogueID, int lastMessageID) {
        return "USE conference;" +
                "SELECT COUNT(*) FROM dbo.messages_dialogue " +
                "WHERE id > " + lastMessageID + " AND dialogues_id = " + dialogueID;
    }
}
