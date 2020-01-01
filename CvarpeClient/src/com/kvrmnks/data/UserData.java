package com.kvrmnks.data;

import sun.security.x509.OIDMap;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserData {
    private static String userName;
    private static String userPassword;
    private static String serverIp,readerIp,writerIp;

    private static final String PERFERRED_IP = "__perferred_ip__.db";
    private static final String PERFERRED_PORT = "__perferrred_port__.db";
    private static final String USERDB = "__user__database__.db";


    private static HashMap<String,String> userDB;
    private static String previousUserName;
    private static ArrayList<String> preferredIp;
    private static ArrayList<String> preferredPort;
    private static String previousIp,previousPort;

    static{

        File file = new File(PERFERRED_IP);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                preferredIp = (ArrayList<String>)objectInputStream.readObject();
                previousIp = (String)objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            preferredIp = new ArrayList<>();
            previousIp = "";
        }

        file = new File(PERFERRED_PORT);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                preferredPort = (ArrayList<String>) objectInputStream.readObject();
                previousPort = (String) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            preferredPort = new ArrayList<>();
            previousPort = "";
        }

        file = new File(USERDB);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                userDB = (HashMap<String, String>) objectInputStream.readObject();
                previousUserName = (String) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            userDB = new HashMap<>();
            previousUserName = "";
        }
    }

    public static void writePreferredIp(){
        File file = new File(PERFERRED_IP);
        if(file.exists())
            file.delete();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(preferredIp);
            objectOutputStream.writeObject(previousIp);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePreferredPort(){
        File file = new File(PERFERRED_PORT);
        if(file.exists())
            file.delete();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(preferredPort);
            objectOutputStream.writeObject(previousPort);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeUserDB(){
        File file = new File(USERDB);
        if(file.exists())
            file.delete();
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(userDB);
            objectOutputStream.writeObject(previousUserName);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        writePreferredIp();
        writePreferredPort();
        writeUserDB();
    }

    public static HashMap<String, String> getUserDB() {
        return userDB;
    }

    public static void setUserDB(HashMap<String, String> userDB) {
        UserData.userDB = userDB;
    }

    public static String getPreviousUserName() {
        return previousUserName;
    }

    public static void setPreviousUserName(String previousUserName) {
        UserData.previousUserName = previousUserName;
    }

    public static String getPreviousIp() {
        return previousIp;
    }

    public static void setPreviousIp(String previousIp) {
        UserData.previousIp = previousIp;
    }

    public static String getPreviousPort() {
        return previousPort;
    }

    public static void setPreviousPort(String previousPort) {
        UserData.previousPort = previousPort;
    }

    public static ArrayList<String> getPreferredIp() {
        return preferredIp;
    }

    public static void setPreferredIp(ArrayList<String> preferredIp) {
        UserData.preferredIp = preferredIp;
    }

    public static ArrayList<String> getPreferredPort() {
        return preferredPort;
    }

    public static void setPreferredPort(ArrayList<String> preferredPort) {
        UserData.preferredPort = preferredPort;
    }

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
