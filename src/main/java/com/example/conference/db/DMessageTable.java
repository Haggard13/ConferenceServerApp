package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.JsonEntity.Input.DialogueID;
import com.example.conference.JsonEntity.Input.DialogueIDList;
import com.example.conference.JsonEntity.Input.DialogueMessage;
import com.example.conference.JsonEntity.Output.*;
import com.example.conference.sqlQuery.DMessageQueries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DMessageTable {
    public static int addMessage(DialogueMessage dm, int type){
        Connection con = ConferenceApplication.con;
        int message_id = -1;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(DMessageQueries.addMessageQuery(dm, type));
            rs.next();
            message_id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return message_id;
    }

    public static MessageNotificationList checkNewMessage(DialogueIDList dialogue_list, int user_id) {
        Connection con = ConferenceApplication.con;

        MessageNotificationList result = new MessageNotificationList();
        result.list = new ArrayList<>();

        try (Statement st = con.createStatement()) {
            for (DialogueID e : dialogue_list.dialogueList) {
                ResultSet rs = st.executeQuery(DMessageQueries.maxMessageIdQuery(e.dialogue_id, user_id));
                rs.next();
                int max_id_message = rs.getInt(1);

                rs = st.executeQuery(DMessageQueries.lastMessageIdQuery(e.dialogue_id, user_id));
                rs.next();
                int last_message_id = rs.getInt(1);

                if (max_id_message > last_message_id) {

                    MessageNotification dmn = new MessageNotification();

                    rs = st.executeQuery(DMessageQueries.messageQuery(max_id_message));
                    rs.next();

                    dmn.text = rs.getString(1);
                    dmn.name = rs.getString(3) + " " + rs.getString(4);
                    dmn.id = rs.getInt(2);

                    result.list.add(dmn);

                    st.execute(DMessageQueries.updateLastMessageIDQuery(max_id_message, e.dialogue_id, user_id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static DMessageList getNewMessage(int user_id, int dialogue_id, int last_message_id) {
        Connection con = ConferenceApplication.con;
        DMessageList dml = new DMessageList();

        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(DMessageQueries.getNewMessageQuery(dialogue_id, last_message_id));
            while (rs.next()) {
                dml.list.add(new DMessage(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getTimestamp(4).getTime(),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(5) == user_id ? 0 : 1,
                        rs.getInt(8)
                ));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return dml;
    }
}
