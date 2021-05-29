package com.example.conference.db;

import com.example.conference.json.Account;
import com.example.conference.ConferenceApplication;
import com.example.conference.json.DialogueEntity;
import com.example.conference.json.Output.OutputDialogue;
import com.example.conference.json.Output.OutputDialogueList;
import com.example.conference.query.DialogueQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DialogueTable {
    public static final Logger logger = LoggerFactory.getLogger(DialogueTable.class);

    public static int createNewDialogue(DialogueEntity dialogue, Account account) {
        Connection con = ConferenceApplication.con;
        int dialogueId = -1;

        try (Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(DialogueQuery.createDialogueQuery(dialogue, account));
            rs.next();
            dialogueId = rs.getInt(1);

            st.execute(DialogueQuery.addCompanion(dialogue.getSecond_user_id(), dialogueId));
            st.execute(DialogueQuery.addCompanion(account.getId(), dialogueId));
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return dialogueId;
    }

    public static OutputDialogueList getNewDialogue(int user_id, int last_dialogue_id) {
        Connection con = ConferenceApplication.con;
        OutputDialogueList ds = new OutputDialogueList();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(DialogueQuery.getNewDialoguesQuery(user_id, last_dialogue_id));

            while (rs.next()) {
                int first_user_id = rs.getInt(2);
                int second_user_id = rs.getInt(3);

                OutputDialogue d;
                if (user_id == first_user_id)
                    d = new OutputDialogue(rs.getInt(1), second_user_id, rs.getString(7), rs.getString(9));
                else
                    d = new OutputDialogue(rs.getInt(1), first_user_id, rs.getString(6), rs.getString(8));
                ds.list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public static List<DialogueEntity> getDialogues(int userID) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()) {
            ResultSet r = st.executeQuery(
                    "USE conference; SELECT * FROM dbo.dialogues " +
                            "WHERE (id in (" +
                            "select id_dialogue from user_dialogues " +
                            "where user_id = " +
                            userID + "))");
            ArrayList<DialogueEntity> dialogues = new ArrayList<>();
            while (r.next()) {
                int id = r.getInt(2);
                dialogues.add(
                        new DialogueEntity(
                                r.getInt(1),
                                r.getInt(id == userID ? 3 : 2),
                                r.getString(id == userID ? 5 : 4),
                                r.getString(id == userID ? 7 : 6),
                                r.getString(id == userID ? 9 : 8),
                                r.getString(10),
                                r.getTimestamp(11).getTime()
                        )
                );
            }
            return dialogues;
        } catch(SQLException e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void setLastMessageAndTime(int id, String text, String time) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()) {
            st.execute("USE conference; UPDATE dbo.dialogues " +
                    "SET last_message = N'" + text + "', " +
                    "last_message_time = '" + time + "' " +
                    "WHERE id = " + id);
        } catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
