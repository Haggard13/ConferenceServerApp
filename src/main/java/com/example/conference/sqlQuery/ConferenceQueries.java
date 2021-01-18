package com.example.conference.sqlQuery;

public class ConferenceQueries {
    public static String notNotifiedConferenceQuery(int user_id) {
        return "USE conference;" +
                "SELECT id, name " +
                    "FROM dbo.conferences " +
                    "WHERE id IN (" +
                        "SELECT id_conference " +
                        "FROM dbo.user_conferences " +
                        "WHERE id_user = " + user_id +
                            " AND notification < 1);";
    }

    public static String updateNotificationQuery(int conference_id, int user_id) {
        return "UPDATE dbo.user_conferences " +
                    "SET notification = 1 " +
                    "WHERE (id_conference = " + conference_id +
                        " AND id_user = " + user_id + ");";
    }

    public static String createConferenceQuery(String name, int count) {
        return "USE conference;" +
                "BEGIN TRANSACTION;" +
                    "INSERT dbo.conferences " +
                    "VALUES('" + name + "', " + count + ");" +
                    "SELECT MAX(id) FROM dbo.conferences;" +
                "COMMIT;";
    }

    public static String addMemberQuery(int id, int conference_id, int status) {
        return "USE conference;" +
                "INSERT dbo.user_conferences " +
                    "VALUES(" + id + ", " + conference_id + ", " + status + ", 0, 0);";
    }

    public static String newConferenceQuery(int user_id, int last_conference_id) {
        return "USE conference;" +
                "SELECT * " +
                    "FROM dbo.conferences " +
                    "WHERE id IN (" +
                        "SELECT id_conference " +
                        "FROM dbo.user_conferences " +
                        "WHERE id_user = " + user_id +
                            " AND id_conference > " + last_conference_id + ")";
    }
}
