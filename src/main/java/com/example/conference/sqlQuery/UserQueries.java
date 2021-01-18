package com.example.conference.sqlQuery;

import com.example.conference.JsonEntity.Output.UserInfo;

public class UserQueries {
    public static String userInfoQuery(int id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM dbo.users " +
                    "WHERE id = " + id + ";";
    }

    public static String addUserQuery(UserInfo userInfo) {
        return "USE conference;" +
                "INSERT dbo.users VALUES ("
                + userInfo.id + ", '" + userInfo.email + "', '" + userInfo.name + "', '" + userInfo.surname + "');";
    }
}
