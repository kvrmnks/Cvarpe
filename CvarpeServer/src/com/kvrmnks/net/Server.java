package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import javafx.application.Platform;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server extends UnicastRemoteObject implements Net {

    private MainController mainController;

    private String getUserName(String pos) {
        return pos.split("/")[1];
    }

    public Server(MainController mainController) throws RemoteException {
        super();
        this.mainController = mainController;
    }

    public boolean logUp(String userName, String userPassword) throws IOException, UserExistedException {
        DataBase.addUser(new User(userName, userPassword));
        Platform.runLater(mainController::flushUserTable);
        Log.log("\n注册账户\n姓名:" + userName + " 密码:" + userPassword);
        return true;
    }

    public boolean logIn(String userName, String userPassword) {
        if (UserManager.checkUser(userName, userPassword)) {
            Log.log("\n用户登录:\n姓名:" + userName + " 密码:" + userPassword);
            return true;
        }
        return false;
    }

    synchronized public void renameFile(String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(pos, name, newName);
    }

    @Override
    synchronized public void renameFile(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(id, pos, name, newName);
    }

    synchronized public void renameFileDirectory(String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(pos, name, newName);
    }

    @Override
    synchronized public void renameFileDirectory(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(id, pos, name, newName);
    }

    synchronized public void deleteFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(pos, name);
    }

    @Override
    synchronized public void deleteFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(id, pos, name);
    }

    synchronized public void deleteFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(pos, name);
    }

    @Override
    synchronized public void deleteFileDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(id, pos, name);
    }

    @Override
    public void createFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFile(id, pos, name);
        disk.mainTain();
    }

    @Override
    public void createFile(long id, String pos, String name, long size) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFile(id,pos,name);
        MyFile myFile = disk.getStructure(id,pos);
        myFile.sonFile.get(name).setSize(size);
        disk.mainTain();
    }

    synchronized public void createDirectory(String pos, String name) throws NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException, IOException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(pos, name);

    }

    @Override
    synchronized public void createDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(id, pos, name);
    }

    @Override
    synchronized public MyFile getStructure(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.getFileById(pos, id);
    }

    @Override
    synchronized public MyFile getStructure(String s) throws NoUserException, NoFileException, ClassNotFoundException, NoAccessException, IOException {
        Disk disk = Disk.constructByUserName(getUserName(s));
        return disk.getStructure(s);

    }


    @Override
    synchronized public int readByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.readByteOfFile(id, pos, name, buffer, begin, length);
    }

    @Override
    public void writeByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeByteOfFile(id, pos, name, buffer, begin, length);
    }

    @Override
    @Deprecated
    synchronized public void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeByteOfFile(pos, name, size, md5, buffer, begin, length);
    }

    @Override
    public InfoFile getInfoFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileStructureException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.getInfoFile(id, pos, name);
    }

    @Override
    public void writeInfoFile(long id, String pos, String name, InfoFile infoFile) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeInfoFile(id, pos, name, infoFile);
    }


    @Override
    synchronized public String getMd5OfFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        MyFile file = getStructure(id, pos);
        if (!file.sonFile.containsKey(name))
            throw new NoFileException();
        file = file.sonFile.get(name);
        return MD5.getMD5OfFile(RealDisk.LOCATION + "__" + file.getId() + "__" + file.getName());
    }

    @Override
    synchronized public MyFile getFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(pos);
        MyFile myFile = disk.getStructure(pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        return myFile.sonFile.get(name);
    }

    @Override
    synchronized public MyFile getFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(pos);
        MyFile myFile = disk.getStructure(pos);
        if (!myFile.sonDirectory.containsKey(name))
            throw new NoFileException();
        return myFile.sonDirectory.get(name);
    }

    @Override
    synchronized public TransDataList getTransDataList(String userName) throws NoSuchUserException {
        return DataBase.getTransDataListByName(userName);
    }

    @Override
    synchronized public String getReadOnlyURL(long id, long fatherId, String pos) {
        return "Viewer^0^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getReadAndWriteURL(long id, long fatherId, String pos) {
        return "Editor^0^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getTempReadOnlyURL(long id, long fatherId, String pos) {
        return "PreViewer^" + MyDate.getNowTimeStamp() + "^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getTempReadAndWriteURL(long id, long fatherId, String pos) {
        return "PreEditor^" + MyDate.getNowTimeStamp() + "^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public void modifyTransDataList(String userName, TransDataList transDataList) throws NoUserException {
        DataBase.setTransDataList(userName, transDataList);
    }


}
