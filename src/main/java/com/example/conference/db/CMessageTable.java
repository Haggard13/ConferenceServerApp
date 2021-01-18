package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.JsonEntity.Input.ConferenceID;
import com.example.conference.JsonEntity.Input.ConferenceIDList;
import com.example.conference.JsonEntity.Input.ConferenceMessage;
import com.example.conference.JsonEntity.Output.MessageNotification;
import com.example.conference.JsonEntity.Output.MessageNotificationList;
import com.example.conference.JsonEntity.Output.CMessage;
import com.example.conference.JsonEntity.Output.CMessageList;
import com.example.conference.sqlQuery.CMessageQueries;

import java.sql.*;
import java.util.ArrayList;

public class CMessageTable {
    public static int addMessage(ConferenceMessage cm, int type) {
        Connection con = ConferenceApplication.con;
        int message_id = -1;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(CMessageQueries.addMessageQuery(cm, type));
            rs.next();
            message_id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return message_id;
    }

    public static MessageNotificationList checkNewMessage(ConferenceIDList conference_list, int user_id) {
        Connection con = ConferenceApplication.con;

        MessageNotificationList result = new MessageNotificationList();
        result.list = new ArrayList<>();

        try(Statement st = con.createStatement()) {
            for (ConferenceID e : conference_list.conferenceList) {
                ResultSet rs = st.executeQuery(CMessageQueries.maxMessageIdQuery(e.conference_id, user_id));
                rs.next();
                int max_id_message = rs.getInt(1);

                rs = st.executeQuery(CMessageQueries.lastMessageIdQuery(e.conference_id, user_id));
                rs.next();
                int last_message_id = rs.getInt(1);


                if (max_id_message > last_message_id) {

                    MessageNotification notification = new MessageNotification();

                    rs = st.executeQuery(CMessageQueries.messageTextQuery(max_id_message));
                    rs.next();

                    notification.text = rs.getString(1);

                    rs = st.executeQuery(CMessageQueries.conferenceIDAndNameQuery(e.conference_id));
                    rs.next();

                    notification.name = rs.getString(2);
                    notification.id = rs.getInt(1);
                    result.list.add(notification);

                    st.execute(CMessageQueries.updateLastMessageIDQuery(max_id_message, e.conference_id, user_id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static CMessageList getNewMessage(int user_id, int conference_id, int l_m_id) {
        Connection con = ConferenceApplication.con;
        CMessageList cml = new CMessageList();

        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(CMessageQueries.getNewMessagesQuery(conference_id, l_m_id));
            while (rs.next()) {
                cml.list.add(new CMessage(
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

        return cml;
    }
}
