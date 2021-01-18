package com.example.conference.sqlQuery;

import com.example.conference.JsonEntity.Dialogue;

public class DialogueQuery {
    public static String notNotifiedDialoguesQuery(int user_id) {
        return "USE conference;" +
                "SELECT id, first_user_id, first_user_name, first_user_surname " +
                    "FROM dbo.dialogues " +
                    "WHERE id IN (" +
                        "SELECT id_dialogue " +
                        "FROM dbo.user_dialogues " +
                        "WHERE user_id = " + user_id + " " +
                            "AND notification < 1);";
    }

    public static String updateNotificationQuery(int ID, int user_id) {
        return "UPDATE dbo.user_dialogues " +
                    "SET notification = 1 " +
                    "WHERE (id_dialogue = " + ID + " " +
                        "AND user_id = " + user_id + ")";
    }

    public static String addCompanion(int user_id, int dialogue_id) {
        return "USE conference;" +
                "INSERT dbo.user_dialogues " +
                    "VALUES(" + user_id + ", " + dialogue_id + ", 0, 0);";
    }

    public static String createDialogueQuery(Dialogue d) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "IF NOT EXISTS(" +
                        "SELECT * " +
                        "FROM dialogues " +
                        "WHERE (first_user_id = " + d.first_user_id + " " +
                                "AND second_user_id = " + d.second_user_id + ") " +
                            "OR (first_user_id = " + d.second_user_id + " " +
                                "AND second_user_id = " + d.first_user_id + ")) " +
                        "BEGIN " +
                            "INSERT dbo.dialogues VALUES(" +
                                d.first_user_id + ", " + d.second_user_id + ", '" +
                                d.first_user_email + "', '" + d.second_user_email + "', '" +
                                d.first_user_name + "', '" + d.second_user_name + "', '" +
                                d.first_user_surname + "', '" + d.second_user_surname + "');" +
                            "SELECT MAX(id) FROM dbo.dialogues; " +
                        "END " +
                "COMMIT;";
    }

    public static String getNewDialoguesQuery(int user_id, int last_dialogue_id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM dbo.dialogues " +
                    "WHERE id IN (" +
                        "SELECT id_dialogue " +
                        "FROM dbo.user_dialogues " +
                        "WHERE user_id = " + user_id + " " +
                            "AND id_dialogue > " + last_dialogue_id + ")";
    }
}
