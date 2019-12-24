package com.kvrmnks.data;

public class MyDate {
    public static String convert(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }

    public static Long getNowTimeStamp() {
        long time = System.currentTimeMillis();
        return time / 1000;
    }

    public static String getCurTime() {
        return convert(System.currentTimeMillis() + "");
    }
}
