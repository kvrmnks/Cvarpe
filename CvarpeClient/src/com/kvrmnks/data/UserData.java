package com.kvrmnks.data;

import com.kvrmnks.exception.NoSuchUserException;
import com.kvrmnks.net.Net;
import com.kvrmnks.net.NetReader;
import com.kvrmnks.net.NetWriter;
import com.kvrmnks.net.sync;
import sun.security.x509.OIDMap;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {
    private static String userName;
    private static String userPassword;
    private static String serverIp,readerIp,writerIp;

    private static final String PERFERRED_IP = "__perferred_ip__.db";
    private static final String PERFERRED_PORT = "__perferrred_port__.db";
    private static final String USERDB = "__user__database__.db";
    private static final String BINDDB = "__binding_data__.db";


    private static ConcurrentHashMap<String,String> userDB;
    private static String previousUserName;
    private static Vector<String> preferredIp;
    private static Vector<String> preferredPort;
    private static ConcurrentHashMap<String, ArrayList<sync>> bindingDB;
    private static String previousIp,previousPort;
    public static NetReader serverReader;
    public static NetWriter serverWriter;
    public static Net server;


    static{

        File file = new File(PERFERRED_IP);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                preferredIp = (Vector<String>)objectInputStream.readObject();
                previousIp = (String)objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            preferredIp = new Vector<>();
            previousIp = "";
        }

        file = new File(PERFERRED_PORT);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                preferredPort = (Vector<String>) objectInputStream.readObject();
                previousPort = (String) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            preferredPort = new Vector<>();
            previousPort = "";
        }

        file = new File(USERDB);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                userDB = (ConcurrentHashMap<String, String>) objectInputStream.readObject();
                previousUserName = (String) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            userDB = new ConcurrentHashMap<>();
            previousUserName = "";
        }
/*
        file = new File(BINDDB);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                bindingDB = (HashMap<String, ArrayList<sync>>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            bindingDB = new HashMap<>();
        }

 */
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

    public static void writeBindDB(){
        File file = new File(BINDDB);
        if(file.exists())
            file.delete();
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(bindingDB);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        writePreferredIp();
        writePreferredPort();
        writeUserDB();
      //  writeBindDB();
    }

    public static ConcurrentHashMap<String, String> getUserDB() {
        return userDB;
    }

    public static void setUserDB(ConcurrentHashMap<String, String> userDB) {
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

    public static Vector<String> getPreferredIp() {
        return preferredIp;
    }

    public static void setPreferredIp(Vector<String> preferredIp) {
        UserData.preferredIp = preferredIp;
    }

    public static Vector<String> getPreferredPort() {
        return preferredPort;
    }

    public static void setPreferredPort(Vector<String> preferredPort) {
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

    public static ArrayList<sync> getBindingListByName(String name) throws NoSuchUserException {
        if(bindingDB == null)
            bindingDB = new ConcurrentHashMap<>();
        if(!bindingDB.containsKey(name)){
            bindingDB.put(name,new ArrayList<sync>());
        }
        return bindingDB.get(name);
    }

    public static void addBindingListByName(String name,sync x) throws NoSuchUserException {
        if(!bindingDB.containsKey(name))
            throw new NoSuchUserException();
        bindingDB.get(name).add(x);
    }
}
