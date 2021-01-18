package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.ResultCard;
import com.example.conference.ResultCards;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResultTable {
    public static int addResult(int conference_id, String name, String description) {
        Connection con = ConferenceApplication.con;
        int result_id = -1;
        try(Statement st = con.createStatement()) {
            st.execute("USE conference; insert results values(" +
                    conference_id + ", '" +
                    name + "', '" +
                    description + "')");
            result_id = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result_id;
    }

    public static ResultCards getResults(int conference_id) {
        Connection con = ConferenceApplication.con;
        ResultCards resultList = new ResultCards(new ArrayList<>());
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("USE conference; " +
                    "SELECT id, name, description FROM results WHERE conference_id = " + conference_id);
            while (rs.next()) {
                resultList.component1().add(
                        new ResultCard(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
