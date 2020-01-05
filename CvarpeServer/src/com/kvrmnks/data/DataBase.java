package com.kvrmnks.data;

import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoSuchUserException;
import com.kvrmnks.exception.NoUserException;
import com.kvrmnks.exception.UserExistedException;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    public static final String LOCATION = "d:/";

    private static ConcurrentHashMap<String, User> userDB;
    private static ConcurrentHashMap<Long, String> fileDB;
    private static ConcurrentHashMap<String, Long> rootDB;
    private static ConcurrentHashMap<String, TransDataList> transData;
    private static String homePath;
    private static ArrayList<String> ports;
    private static ConcurrentHashMap<String, TreeSet<String>> shareList;
    private static String preferredPort;
    private static ConcurrentHashMap<String,UserDisk> userDisk;
    private static ConcurrentHashMap<Long,MyFile> fileDisk;

    private static final String USERDB = "__userDB__.db";
    private static final String FILEDB = "__fileDB__.db";
    private static final String ROOTDB = "__rootDB__.db";
    private static final String FILECOUNT = "__fileCount__.db";
    private static final String TRANS_DATA = "__transData__.db";
    private static final String FILE_STORE_DATA = "__storeData__.db";
    private static final String PREFERRED_PORT = "__preferredPort__.db";
    private static final String SHARE_DATA = "__shareData__.db";
    private static final String USER_DISK = "__userDisk__.db";
    private static final String File_DISK = "__fileDisk__.db";

    private static long fileCount;

    static {
        File file = new File(USERDB);
        if (file.exists()) {
            try {
                userDB = (ConcurrentHashMap<String, User>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            userDB = new ConcurrentHashMap<>();
        }

        file = new File(FILEDB);
        if (file.exists()) {
            try {
                fileDB = (ConcurrentHashMap<Long, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            fileDB = new ConcurrentHashMap<>();
        }

        file = new File(ROOTDB);
        if (file.exists()) {
            try {
                rootDB = (ConcurrentHashMap<String, Long>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            rootDB = new ConcurrentHashMap<>();
        }

        file = new File(FILECOUNT);
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
                transData = (ConcurrentHashMap<String, TransDataList>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                transData = new ConcurrentHashMap<>();
                e.printStackTrace();
            }
        } else {
            transData = new ConcurrentHashMap<>();
        }

        file = new File(FILE_STORE_DATA);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                homePath = (String) objectInputStream.readObject();
                preferredPort = (String) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                homePath = "";
                e.printStackTrace();
            }
        } else {
            homePath = "";
            preferredPort = "";
        }
        Disk.LOCATION = homePath;

        file = new File(PREFERRED_PORT);
        if (file.exists()) {
            try {
                ports = (ArrayList<String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            ports = new ArrayList<>();
        }

        file = new File(SHARE_DATA);
        if (file.exists()) {
            try {
                shareList = (ConcurrentHashMap<String, TreeSet<String>>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            shareList = new ConcurrentHashMap<>();
        }

        file = new File(USER_DISK);
        if(file.exists()){
            try{
                userDisk = (ConcurrentHashMap<String,UserDisk>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            userDisk = new ConcurrentHashMap<>();
        }

        file = new File(File_DISK);
        if(file.exists()){
            try{
                fileDisk = (ConcurrentHashMap<Long,MyFile>) new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            fileDisk = new ConcurrentHashMap<>();
        }
    }

    synchronized public static long getCount() {
        return ++fileCount;
    }

    public static void wirteUserDB() {
        File file = new File(USERDB);
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(userDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wirteFileDB() {
        File file = new File(FILEDB);
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(fileDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRootDB() {
        File file = new File(ROOTDB);
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(rootDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileCountDB() {
        File file = new File(FILECOUNT);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(fileCount);
            //objectOutputStream.writeObject(preferredPort);
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

    public static void writeFileStoreDB() {
        File file = new File(FILE_STORE_DATA);
        if (file.exists())
            file.delete();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(homePath);
            objectOutputStream.writeObject(preferredPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePreferredPort() {
        File file = new File(PREFERRED_PORT);
        if (file.exists()) {
            file.delete();
        }
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(ports);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeShareList() {
        File file = new File(SHARE_DATA);
        if (file.exists())
            file.delete();
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(shareList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeUserDisk(){
        File file = new File(USER_DISK);
        if(file.exists())
            file.delete();
        try{
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(userDisk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileDisk(){
        File file = new File(File_DISK);
        if(file.exists())
            file.delete();
        try{
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(fileDisk);
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
        writeFileStoreDB();
        writePreferredPort();
        writeShareList();
        writeUserDisk();
        writeFileDisk();
    }

    public static void addUser(User user) throws UserExistedException, IOException {
        if (userDB.containsKey(user.getName()))
            throw new UserExistedException();
        userDB.put(user.getName(), user);
        MyFile myFile = new MyFile();
        myFile.setId(getCount());
        myFile.setType(MyFile.TYPEFILEDERECTORY);
        myFile.setName(user.getName());
        myFile.setModifyTime(MyDate.getCurTime());
        DataBase.addUserDisk(user.getName(),new UserDisk(myFile));
        DataBase.addFile(myFile,true);
        //RealDisk.writeMyFile(myFile, myFile.getId(), myFile.getName() + ".db");
        //fileDB.put(myFile.getId(), myFile.getName());
        rootDB.put(user.getName(), myFile.getId());
    }

    public static String getPassword(String userName) throws NoUserException {
        if (!userDB.containsKey(userName))
            throw new NoUserException();
        return userDB.get(userName).getPassword();
    }

    public static User[] getAllUser() {
        User[] ret = new User[userDB.size()];
        Collection<User> r = userDB.values();
        r.toArray(ret);
        return ret;
        /*
        Set<Map.Entry<String, String>> set = userDB.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        int cnt = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> cur = it.next();
            ret[cnt++] = new User(cur.getKey(), cur.getValue());
        }
        return ret;
         */
    }

    public static void modifyUserUsedCapacity(String name, long size) throws NoSuchUserException {
        if (!userDB.containsKey(name))
            throw new NoSuchUserException();
        userDB.get(name).setUsedCapacity(size);
    }

    public static void modifyUserCapacity(String name, long size) throws NoSuchUserException {
        if (!userDB.containsKey(name))
            throw new NoSuchUserException();
        userDB.get(name).setCapacity(size);
    }

    public static void modifyUserPassword(String userName, String newPassword) throws NoUserException {
        if (!userDB.containsKey(userName))
            throw new NoUserException();
        User x = userDB.get(userName);
        x.setPassword(newPassword);
        //userDB.replace(userName, newPassword);
    }

    public static void modifyUserLastLogin(String userName, long time) throws NoSuchUserException {
        if(!userDB.containsKey(userName))
            throw new NoSuchUserException();
        User x = userDB.get(userName);
        x.setLastLogin(time);
    }

    public static void deleteUser(String name) throws NoUserException {
        if (!userDB.containsKey(name))
            throw new NoUserException();
        userDB.remove(name);
    }

    public static void addFile(MyFile myFile) {
        myFile.setId(getCount());
        fileDB.put(myFile.getId(), myFile.getName());
        fileDisk.put(myFile.getId(),myFile);
    }

    public static void addFile(MyFile myFile,boolean flag) {
        //myFile.setId(getCount());
        fileDB.put(myFile.getId(), myFile.getName());
        fileDisk.put(myFile.getId(),myFile);
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
            fileDisk.remove(id);
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
        if (!transData.containsKey(userName))
            throw new NoUserException();
        transData.replace(userName, transDataList);
    }

    public static String getHomePath() {
        Disk.LOCATION = homePath;
        return homePath;
    }

    public static void setHomePath(String homePath) {
        Disk.LOCATION = homePath+"/";
        DataBase.homePath = homePath+"/";
    }

    public static ArrayList<String> getPorts() {
        return ports;
    }

    public static void setPorts(ArrayList<String> ports) {
        DataBase.ports = ports;
    }

    public static String getPreferredPort() {
        return DataBase.preferredPort;
        //return DataBase.getPreferredPort();
    }

    public static void setPreferredPort(String content) {
        DataBase.preferredPort = content;
    }

    public static void addShareList(String name, String content) {
        if (!shareList.containsKey(name)) {
            shareList.put(name, new TreeSet<>());
        }
        TreeSet<String> ret = shareList.get(name);
        ret.add(content);
    }

    public static String[] getShareList(String name) {
        TreeSet<String> ret = shareList.getOrDefault(name, new TreeSet<>());
        String[] arg = new String[ret.size()];
        ret.toArray(arg);
        return arg;
    }

    public static void deleteShareList(String name, String content) {
        if (!shareList.containsKey(name)) {
            return;
        }
        TreeSet<String> ret = shareList.get(name);
        ret.remove(content);
    }

    public static User getUser(String name) throws NoSuchUserException {
        if(!userDB.containsKey(name))
            throw new NoSuchUserException();
        return userDB.get(name);
    }

    public static void addUserDisk(String name,UserDisk userDisk) throws UserExistedException {
        if(DataBase.userDisk.containsKey(name))
            throw new UserExistedException();
        DataBase.userDisk.put(name,userDisk);
    }

    public static UserDisk getUserDiskByName(String name) throws NoSuchUserException {
        if(!userDisk.containsKey(name))
            throw new NoSuchUserException();
        return userDisk.get(name);
    }

    public static MyFile getMyFileById(long id) throws NoFileException {
        if(!fileDisk.containsKey(id))
            throw new NoFileException();
        return fileDisk.get(id);
    }

    public static void replaceMyFile(long id,MyFile mf){
        fileDisk.replace(id,mf);
    }
}