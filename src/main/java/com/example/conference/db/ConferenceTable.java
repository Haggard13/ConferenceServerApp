package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.JsonEntity.Input.Conference;
import com.example.conference.JsonEntity.Input.ConferenceMember;
import com.example.conference.JsonEntity.Output.*;
import com.example.conference.sqlQuery.ConferenceQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConferenceTable {
    public static ConferenceNotification checkNewConference(int user_id) {
        Connection con = ConferenceApplication.con;
        ConferenceNotification cs = new ConferenceNotification();

        try(Statement st = con.createStatement()) {
            List<Integer> conferenceIDList = new ArrayList<>();

            ResultSet rs = st.executeQuery(ConferenceQueries.notNotifiedConferenceQuery(user_id));
            while (rs.next()) {
                cs.conference_list.add(rs.getString(2));
                conferenceIDList.add(rs.getInt(1));
            }
            for (Integer conference_id: conferenceIDList) {
                st.execute(ConferenceQueries.updateNotificationQuery(conference_id, user_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cs;
    }

    public static int createNewConference(Conference c) {
        Connection con = ConferenceApplication.con;
        int conference_id = -1;

        try (Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(ConferenceQueries.createConferenceQuery(c.name, c.count));
            rs.next();
            conference_id = rs.getInt(1);
            for (ConferenceMember conferenceMember: c.members) {
                st.execute(ConferenceQueries.addMemberQuery(conferenceMember.id,  conference_id, conferenceMember.status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conference_id;
    }

    public static OutputConferenceList getNewConference(int user_id, int last_conference_id) {
        Connection con = ConferenceApplication.con;
        OutputConferenceList conferences = new OutputConferenceList();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(ConferenceQueries.newConferenceQuery(user_id, last_conference_id));
            conferences.list = new ArrayList<>();
            while (rs.next()) {
                OutputConference c = new OutputConference(rs.getInt(1), rs.getString(2), rs.getInt(3));
                conferences.list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conferences;
    }

    public static int renameConference(int id, String name) {
        Connection con = ConferenceApplication.con;
        int code = -1;

        try(Statement st = con.createStatement()) {
            st.execute("USE conference;" +
                    " UPDATE conferences SET name = '" + name + "' WHERE id = " + id);
            code = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return code;
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

    public static ConferenceMembersList getConferenceMembers(int id) {
        Connection con = ConferenceApplication.con;
        ConferenceMembersList cml = new ConferenceMembersList();
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT users.email, " +
                            "users.name, " +
                            "users.surname, " +
                            "user_conferences.status " +
                            "FROM users INNER JOIN user_conferences ON users.id = user_conferences.id_user " +
                            "WHERE user_conferences.id_conference = " + id);
            while (rs.next()) {
                cml.list.add(
                        new ContactEntityWithStatus(rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cml;
    }

    public static int deleteUser(int id, int user_id, int deleter_id) {
        Connection con = ConferenceApplication.con;
        int result = 0;
        try (Statement st = con.createStatement()){
            st.execute("IF EXISTS (SELECT status FROM user_conferences WHERE (id_user = " + deleter_id + " AND id_conference = " + id +
                    " AND status = 1)) DELETE FROM user_conferences WHERE id_conference = " + id + " AND id_user = " + user_id);
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int addUser(int id, int user_id, int adder_id) {
        Connection con = ConferenceApplication.con;
        int result = 0;
        try (Statement st = con.createStatement()){
            st.execute("IF EXISTS (SELECT status FROM user_conferences WHERE (id_user = " + adder_id + " AND id_conference = " + id +
                    " AND status = 1)) INSERT user_conferences VALUES (" + user_id + ", " + id + ", 0, 0, 0)");
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
