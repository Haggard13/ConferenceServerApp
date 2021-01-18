package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.JsonEntity.Output.UserInfo;
import com.example.conference.sqlQuery.UserQueries;

import java.sql.*;

public class UserTable {
    public static UserInfo getUserInfo(int id) {
        Connection con = ConferenceApplication.con;
        UserInfo user_info = null;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(UserQueries.userInfoQuery(id));

            rs.next();
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            user_info = new UserInfo(id, email, name, surname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_info;
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
}
