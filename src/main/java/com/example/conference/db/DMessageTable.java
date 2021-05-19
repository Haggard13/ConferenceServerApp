package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.DMessageEntity;
import com.example.conference.query.DMessageQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DMessageTable {
    private static final Logger log = LoggerFactory.getLogger(DMessageTable.class);

    public static int addMessage(DMessageEntity message){
        Connection con = ConferenceApplication.con;
        int message_id = -1;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(DMessageQueries.addMessageQuery(message));
            rs.next();
            message_id = rs.getInt(1);
        } catch (SQLException e) {
            log.error(
                    "Message sending failed: message = {}, user = {}, dialogue = {}. Reason: {}",
                    message.getText(),
                    message.getSender_id(),
                    message.getDialogue_id(),
                    e.getMessage()
            );
        }

        return message_id;
    }

    public static boolean checkNewMessages(int dialogueID, int lastMessageID) {
        Connection connection = ConferenceApplication.con;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(DMessageQueries.checkNewMessages(dialogueID, lastMessageID));
            resultSet.next();
            return resultSet.getInt(1) != 0;
        } catch (SQLException e) {
            log.error("Check new message: SQLException {}", e.getMessage());
        }
        return false;
    }

    public static List<DMessageEntity> getNewMessage(int user_id, int dialogue_id, int last_message_id) {
        Connection con = ConferenceApplication.con;
        List<DMessageEntity> messageEntities = new ArrayList<>();

        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(DMessageQueries.getNewMessageQuery(dialogue_id, last_message_id));
            while (rs.next()) {
                messageEntities.add(new DMessageEntity(
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
}
