package com.example.conference.query;

import com.example.conference.Account;
import com.example.conference.json.DialogueEntity;

import java.text.SimpleDateFormat;

public class DialogueQuery {

    public static String addCompanion(int user_id, int dialogue_id) {
        return "USE conference;" +
                "INSERT dbo.user_dialogues " +
                    "VALUES(" + user_id + ", " + dialogue_id + ", 0, 0);";
    }

    public static String createDialogueQuery(DialogueEntity dialogue, Account account) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "IF NOT EXISTS(" +
                        "SELECT * " +
                        "FROM dialogues " +
                        "WHERE (first_user_id = " + account.getId() + " " +
                                "AND second_user_id = " + dialogue.getSecond_user_id() + ") " +
                            "OR (first_user_id = " + dialogue.getSecond_user_id() + " " +
                                "AND second_user_id = " + account.getId() + ")) " +
                        "BEGIN " +
                            "INSERT dbo.dialogues VALUES(" +
                                account.getId() + ", " + dialogue.getSecond_user_id() + ", N'" +
                                account.getEmail() + "', N'" + dialogue.getSecond_user_email() + "', N'" +
                                account.getName() + "', N'" + dialogue.getSecond_user_name() + "', N'" +
                                account.getSurname() + "', N'" + dialogue.getSecond_user_surname() + "', N'" +
                                dialogue.getLast_message() +"', '" + getTime(dialogue.getLast_message_time()) + "'); " +
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

    private static String getTime(long ms) {
        return new SimpleDateFormat("yyyy-MM-dd").format(ms) +
                "T" +
                new SimpleDateFormat("HH:mm:ss.SSS").format(ms);
    }
}
