package com.kvrmnks.data;

public class UserData {
    private static String userName;
    private static String userPassword;
    private static String ip;

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

    public static void setIp(String ip){
        UserData.ip = ip;
    }

    public static String getIp(){
        return ip;
    }
}
