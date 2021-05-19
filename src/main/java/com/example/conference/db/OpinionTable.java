package com.example.conference.db;

import com.example.conference.ConferenceApplication;
import com.example.conference.json.Opinion;
import com.example.conference.json.OpinionCard;
import com.example.conference.json.OpinionCards;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OpinionTable {
    public static int addOpinion(
            int user_id,
            String text,
            int type,
            int result_id,
            String user_name,
            String user_surname
    ) {
        Connection con = ConferenceApplication.con;
        int result = -1;
        try(Statement st = con.createStatement()) {
            st.execute("USE conference; INSERT opinions VALUES (" +
                    user_id + ", '" +
                    text + "', " +
                    type +", " +
                    result_id + ", '" +
                    user_name + "', '" +
                    user_surname + "')");
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static OpinionCards getOpinions(int result_id) {
        Connection con = ConferenceApplication.con;
        OpinionCards result = new OpinionCards(new ArrayList());
        try(Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("USE conference; " +
                    "SELECT user_id, user_name, user_surname FROM opinions WHERE result_id = " + result_id);
            while (rs.next()) {
                result.component1().add(new OpinionCard(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Opinion getOpinion(int user_id, int result_id) {
        Connection con = ConferenceApplication.con;
        Opinion result = null;
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("USE conference; " +
                    "select text, type from opinions where user_id = " + user_id + " AND result_id = " + result_id);
            rs.next();
            result = new Opinion(rs.getString(1), rs.getInt(2));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
