package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.JsonEntity.Dialogue;
import com.example.conference.JsonEntity.Output.DialogueNotification;
import com.example.conference.JsonEntity.Output.OutputDialogue;
import com.example.conference.JsonEntity.Output.OutputDialogueList;
import com.example.conference.sqlQuery.DialogueQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DialogueTable {

    public static DialogueNotification checkNewDialogue(int user_id) {
        Connection con = ConferenceApplication.con;
        DialogueNotification ds = new DialogueNotification();

        try (Statement st = con.createStatement()) {
            List<Integer> dialoguesID = new ArrayList<>();

            ResultSet rs = st.executeQuery(DialogueQuery.notNotifiedDialoguesQuery(user_id));

            while (rs.next()) {
                if (rs.getInt(2) == user_id)
                    continue;
                else
                    ds.dialogue_list.add(rs.getString(3) + " " + rs.getString(4));
                dialoguesID.add(rs.getInt(1));
            }

            for (Integer ID : dialoguesID)
                st.execute(DialogueQuery.updateNotificationQuery(ID, user_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static int createNewDialogue(Dialogue d) {
        Connection con = ConferenceApplication.con;
        int dialogue_id = -1;

        try (Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(DialogueQuery.createDialogueQuery(d));
            rs.next();
            dialogue_id = rs.getInt(1);

            st.execute(DialogueQuery.addCompanion(d.first_user_id, dialogue_id));
            st.execute(DialogueQuery.addCompanion(d.second_user_id, dialogue_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dialogue_id;
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
}
