package com.example.conference.query;

import com.example.conference.json.Output.UserInfo;

public class UserQueries {
    public static String userInfoQueryDeprecated(int id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM dbo.users " +
                    "WHERE id = " + id + ";";
    }

    public static String addUserQuery(UserInfo userInfo) {
        return "USE conference;" +
                "INSERT dbo.users VALUES ("
                + userInfo.id + ", N'" + userInfo.email + "', N'" + userInfo.name + "', N'" + userInfo.surname + "');";
    }

    public static String userInfoQuery(String email) {
        return "USE conference;" +
                "SELECT name, surname " +
                "FROM dbo.users " +
                "WHERE email = '" + email + "';";
    }

    public static String addToken(int userID, String token) {
        return "USE conference;" +
                "INSERT users_tokens VALUES(" + userID +", N'" + token + "')";
    }
}
