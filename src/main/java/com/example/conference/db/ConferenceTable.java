package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.ConferenceEntity;
import com.example.conference.json.Input.ConferenceMember;
import com.example.conference.json.Output.ContactEntityWithStatus;
import com.example.conference.query.ConferenceQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConferenceTable {
    private static Logger logger = LoggerFactory.getLogger(ConferenceTable.class);

    public static int createNewConference(ConferenceEntity conference, List<ConferenceMember> members) {
        Connection con = ConferenceApplication.con;

        try (Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(ConferenceQueries.createConferenceQuery(conference));
            rs.next();
            int conferenceID = rs.getInt(1);
            for (ConferenceMember member : members) {
                st.execute(ConferenceQueries.addMemberQuery(member,  conferenceID));
            }
            return conferenceID;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return -1;
    }

    public static boolean renameConference(int id, String name) {
        Connection con = ConferenceApplication.con;

        try(Statement st = con.createStatement()) {
            st.execute("USE conference;" +
                    " UPDATE conferences SET name = N'" + name + "' WHERE id = " + id);
            logger.info("conference rename");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    public static String getConferenceName(int id) {
        Connection con = ConferenceApplication.con;
        String name = "";

        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("USE conference;" +
                    " Select name From conferences WHERE id = " + id);
            rs.next();
            name = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static List<ContactEntityWithStatus> getConferenceMembers(int id) {
        Connection con = ConferenceApplication.con;
        List<ContactEntityWithStatus> members = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT users.email, " +
                            "users.name, " +
                            "users.surname, " +
                            "user_conferences.status " +
                            "FROM users INNER JOIN user_conferences ON users.id = user_conferences.id_user " +
                            "WHERE user_conferences.id_conference = " + id);
            while (rs.next()) {
                members.add(
                        new ContactEntityWithStatus(rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getInt(4)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return members;
    }

    public static boolean deleteUser(int id, int user_id, int deleter_id) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()){
            st.execute("IF EXISTS (SELECT status FROM user_conferences WHERE (id_user = " + deleter_id + " AND id_conference = " + id +
                    " AND status = 1)) DELETE FROM user_conferences WHERE id_conference = " + id + " AND id_user = " + user_id);
            logger.info("User deleted from conference");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addUser(int id, int user_id, int adder_id) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()){
            st.execute("IF EXISTS (SELECT status FROM user_conferences WHERE (id_user = " + adder_id + " AND id_conference = " + id +
                    " AND status = 1)) INSERT user_conferences VALUES (" + user_id + ", " + id + ", 0, 0, 0)");
            logger.info("User added in conference");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<ConferenceEntity> getConferences(int userID) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()) {
            ResultSet r = st.executeQuery(
                    "USE conference; SELECT * FROM dbo.conferences " +
                    "WHERE (id in (" +
                    "select id_conference from user_conferences " +
                    "where id_user = " +
                    userID + "))");
            ArrayList<ConferenceEntity> conferences = new ArrayList<>();
            while (r.next()) {
                conferences.add(
                        new ConferenceEntity(
                                r.getInt(1),
                                r.getString(2),
                                r.getInt(3),
                                r.getString(4),
                                r.getTimestamp(5).getTime()
                        )
                );
            }
            return conferences;
        } catch(SQLException e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void setLastMessageAndTime(int id, String text, String time) {
        Connection con = ConferenceApplication.con;
        try (Statement st = con.createStatement()) {
            st.execute("USE conference; UPDATE dbo.conferences " +
                    "SET last_message = N'" + text + "', " +
                    "last_message_time = '" + time + "' " +
                    "WHERE id = " + id);
        } catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
