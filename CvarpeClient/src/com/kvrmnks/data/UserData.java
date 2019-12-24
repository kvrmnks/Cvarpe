package com.kvrmnks.data;

public class UserData {
    public static String userName;
    public static String userPassword;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserData.userName = userName;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static void setUserPassword(String userPassword) {
        UserData.userPassword = userPassword;
    }
}
