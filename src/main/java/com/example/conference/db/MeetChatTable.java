package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.ConferenceMessageWithoutID;
import com.example.conference.json.Output.CMessage;
import com.example.conference.json.Output.CMessageList;
import com.example.conference.query.MeetChatQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeetChatTable {
    private static final Logger log = LoggerFactory.getLogger(MeetChatTable.class);

    public static int sendMessage(ConferenceMessageWithoutID message) {
        Connection con = ConferenceApplication.con;
        int message_id = -1;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(MeetChatQueries.sendMessage(message));
            rs.next();
            message_id = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Send message: SQLException");
        }

        return message_id;
    }

    /**
     * Check new message in conference
     * @param conferenceID Conference ID for checking
     * @return True - if new message contains, else - false
     */
    public static boolean checkNewMessage(int conferenceID, int lastMessageID) {
        Connection connection = ConferenceApplication.con;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(MeetChatQueries.checkNewMessages(conferenceID, lastMessageID));
            resultSet.next();
            return resultSet.getInt(1) != 0;
        } catch (SQLException e) {
            log.error("Check new message: SQLException");
        }
        return false;
    }

    /**
     * Return list of new messages in meet chat
     * @param userID - user ID for sender type
     * @param conferenceID - conference where place meet chat
     * @param lastMessageID - id of last message in user device
     * @return new message list
     */
    public static CMessageList getNewMessages(int userID, int conferenceID, int lastMessageID) {
        Connection connection = ConferenceApplication.con;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(MeetChatQueries.getNewMessages(conferenceID, lastMessageID));

            CMessageList newMessages = new CMessageList();
            while (resultSet.next()) {
                CMessage newMessage = new CMessage(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getTimestamp(4).getTime(),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(5) == userID ? 0 : 1,
                        resultSet.getInt(8));
                newMessages.list.add(newMessage);
            }
            return newMessages;
        } catch (SQLException e) {
            log.error("Get new messages: SQLException");
        }
        return null;
    }
}
