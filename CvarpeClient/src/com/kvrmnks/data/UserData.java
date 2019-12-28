package com.kvrmnks.data;

public class UserData {
    private static String userName;
    private static String userPassword;
    private static String serverIp,readerIp,writerIp;

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

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        UserData.serverIp = serverIp;
    }

    public static String getReaderIp() {
        return readerIp;
    }

    public static void setReaderIp(String readerIp) {
        UserData.readerIp = readerIp;
    }

    public static String getWriterIp() {
        return writerIp;
    }

    public static void setWriterIp(String writerIp) {
        UserData.writerIp = writerIp;
    }
}
