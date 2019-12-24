package com.kvrmnks.data;

import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoSuchUserException;
import com.kvrmnks.exception.NoUserException;
import com.kvrmnks.exception.UserExistedException;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DataBase {
    public static final String LOCATION = "d:/";

    private static HashMap<String, String> userDB;

    private static HashMap<Long, String> fileDB;

    private static HashMap<String, Long> rootDB;

    private static HashMap<String, TransDataList> transData;

    private static final String TRANS_DATA = "transData.db";

    private static long fileCount;

    static {
        File file = new File("__userDB__.db");
        if (file.exists()) {
            try {
                userDB = (HashMap<String, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            userDB = new HashMap<>();
        }

        file = new File("__fileDB__.db");
        if (file.exists()) {
            try {
                fileDB = (HashMap<Long, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            fileDB = new HashMap<>();
        }

        file = new File("__rootDB__.db");
        if (file.exists()) {
            try {
                rootDB = (HashMap<String, Long>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            rootDB = new HashMap<>();
        }

        file = new File("__fileCount__.db");
        if (file.exists()) {
            try {
                fileCount = (Long) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            fileCount = 0;
        }

        file = new File(TRANS_DATA);
        if (file.exists()) {
            try {
                transData = (HashMap<String, TransDataList>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                transData = new HashMap<>();
                e.printStackTrace();
            }
        } else {
            transData = new HashMap<>();
        }

    }

    synchronized public static long getCount() {
        return ++fileCount;
    }

    public static void wirteUserDB() {
        File file = new File("__userDB__.db");
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(userDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wirteFileDB() {
        File file = new File("__fileDB__.db");
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(fileDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRootDB() {
        File file = new File("__rootDB__.db");
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(rootDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileCountDB() {
        File file = new File("__fileCount__.db");
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(fileCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTransDataDB() {
        File file = new File(TRANS_DATA);
        if (file.exists())
            file.delete();
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(transData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDB() {
        wirteUserDB();
        wirteFileDB();
        writeRootDB();
        writeFileCountDB();
        writeTransDataDB();
    }

    public static void addUser(User user) throws UserExistedException, IOException {
        if (userDB.containsKey(user.getName()))
            throw new UserExistedException();
        userDB.put(user.getName(), user.getPassword());
        MyFile myFile = new MyFile();
        myFile.setId(getCount());
        myFile.setType(MyFile.TYPEFILEDERECTORY);
        myFile.setName(user.getName());

        RealDisk.writeMyFile(myFile, myFile.getId(), myFile.getName() + ".db");
        fileDB.put(myFile.getId(), myFile.getName());
        rootDB.put(user.getName(), myFile.getId());
    }

    public static String getPassword(String userName) throws NoUserException {
        if (!userDB.containsKey(userName))
            throw new NoUserException();
        return userDB.get(userName);
    }

    public static User[] getAllUser() {
        User[] ret = new User[userDB.size()];
        Set<Map.Entry<String, String>> set = userDB.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        int cnt = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> cur = it.next();
            ret[cnt++] = new User(cur.getKey(), cur.getValue());
        }
        return ret;
    }

    public static void modifyUserPassword(String userName, String newPassword) throws NoUserException {
        if (!userDB.containsKey(userName))
            throw new NoUserException();
        userDB.replace(userName, newPassword);
    }

    public static void deleteUser(String name) throws NoUserException {
        if (!userDB.containsKey(name))
            throw new NoUserException();
        userDB.remove(name);
    }

    public static void addFile(MyFile myFile) {
        myFile.setId(getCount());
        fileDB.put(myFile.getId(), myFile.getName());
    }

    public static long getRoot(String userName) throws NoUserException {
        if (!rootDB.containsKey(userName))
            throw new NoUserException();
        return rootDB.get(userName);
    }

    public static String getFile(long id) throws NoFileException {
        if (!fileDB.containsKey(id))
            throw new NoFileException();
        return fileDB.get(id);
    }

    public static void deleteFile(long id) throws NoFileException {
        if (fileDB.containsKey(id)) {
            fileDB.remove(id);
        } else {
            throw new NoFileException();
        }
    }

    public static TransDataList getTransDataListByName(String str) throws NoSuchUserException {
        if (transData.containsKey(str))
            return transData.get(str);
        else
            throw new NoSuchUserException();
    }

    public static void addTransDataList(String userName, TransDataList transDataList) {
        if (transData.containsKey(userName))
            transData.replace(userName, transDataList);
        else
            transData.put(userName, transDataList);
    }

    public static void setTransDataList(String userName, TransDataList transDataList) throws NoUserException {
        if(!transData.containsKey(userName))
            throw new NoUserException();
        transData.replace(userName,transDataList);
    }
}