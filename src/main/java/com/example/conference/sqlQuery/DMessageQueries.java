package com.example.conference.sqlQuery;

import com.example.conference.JsonEntity.Input.DialogueMessage;

public class DMessageQueries {
    public static String addMessageQuery(DialogueMessage dm, int type) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.messages_dialogue VALUES('" +
                    dm.text + "', " +
                    dm.dialogue_id + ", '" +
                    dm.date_time + "', " +
                    dm.sender_id + ", '" +
                    dm.sender_name + "', '" +
                    dm.sender_surname + "', " +
                    type + ")" +
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
}
