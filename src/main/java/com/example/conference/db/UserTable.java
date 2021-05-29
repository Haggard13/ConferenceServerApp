package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.ContactEntity;
import com.example.conference.json.Output.UserInfo;
import com.example.conference.query.UserQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {
    private static Logger log = LoggerFactory.getLogger(UserTable.class);

    public static UserInfo getUserInfoDeprecated(int id) {
        Connection con = ConferenceApplication.con;
        UserInfo user_info = null;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(UserQueries.userInfoQueryDeprecated(id));

            rs.next();
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            user_info = new UserInfo(id, email, name, surname);
        } catch (SQLException e) {
            log.error("User {} not found", id);
        }
        return user_info;
    }

    public static ContactEntity getUserInfo(String email) {
        Connection con = ConferenceApplication.con;
        ContactEntity user = null;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(UserQueries.userInfoQuery(email));

            rs.next();
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            user = new ContactEntity(email, name, surname);
        } catch (SQLException e) {
            log.error("User {} not found", email);
        }
        return user;
    }

    public static int addUser(UserInfo userInfo) {
        Connection con = ConferenceApplication.con;
        int success = -1;

        try (Statement st = con.createStatement()) {
            st.execute(UserQueries.addUserQuery(userInfo));
            success = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static void addToken(int userID, String token) {
        Connection con = ConferenceApplication.con;

        try (Statement st = con.createStatement()) {
            st.execute(UserQueries.addToken(userID, token));
        } catch (SQLException e) {
            log.warn("Token adding failed. Reason: {}", e.getMessage());
        }
    }
}
