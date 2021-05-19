package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.CMessageEntity;
import com.example.conference.query.CMessageQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CMessageTable {
    private static final Logger log = LoggerFactory.getLogger(CMessageTable.class);

    public static int addMessage(CMessageEntity conferenceMessage) {
        Connection con = ConferenceApplication.con;
        int messageId = -1;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(CMessageQueries.addMessageQuery(conferenceMessage));
            rs.next();
            messageId = rs.getInt(1);
        } catch (SQLException e) {
            log.error(
                    "Message sending failed: message = {}, user = {}, conference = {}. Reason: {}",
                    conferenceMessage.getText(),
                    conferenceMessage.getSender_id(),
                    conferenceMessage.getConference_id(),
                    e.getMessage()
            );
        }

        return messageId;
    }

    public static List<CMessageEntity> getNewMessage(int user_id, int conference_id, int l_m_id) {
        Connection con = ConferenceApplication.con;
        List<CMessageEntity> messageEntities = new ArrayList<>();

        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(CMessageQueries.getNewMessagesQuery(conference_id, l_m_id));
            while (rs.next()) {
                messageEntities.add(new CMessageEntity(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(4).getTime(),
                        rs.getInt(5),
                        rs.getInt(3),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(5) == user_id ? 0 : 1,
                        rs.getInt(8)
                ));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return messageEntities;
    }

    public static boolean checkNewMessages(int conferenceID, int lastMessageID) {
        Connection connection = ConferenceApplication.con;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(CMessageQueries.checkNewMessages(conferenceID, lastMessageID));
            resultSet.next();
            return resultSet.getInt(1) != 0;
        } catch (SQLException e) {
            log.error("Check new message: SQLException");
        }
        return false;
    }
}
