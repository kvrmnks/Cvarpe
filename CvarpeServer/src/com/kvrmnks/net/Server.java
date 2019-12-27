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

public class Server extends UnicastRemoteObject implements Net{

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

    synchronized public void downloadFile(String pos, String name) {

    }

    synchronized public void uploadFile(String pos, String name) {

    }

    synchronized public void renameFile(String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(pos, name, newName);
    }

    @Override
    synchronized public void renameFile(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(id,pos,name,newName);
    }

    synchronized public void renameFileDirectory(String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(pos, name, newName);
    }

    @Override
    synchronized public void renameFileDirectory(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(id,pos, name, newName);
    }

    synchronized public void deleteFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(pos, name);
    }

    @Override
    synchronized public void deleteFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(id,pos, name);
    }

    synchronized public void deleteFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(pos, name);
    }

    @Override
    synchronized public void deleteFileDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(id,pos, name);
    }

    synchronized public void createDirectory(String pos, String name) throws NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException, IOException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(pos, name);

    }

    @Override
    synchronized public void createDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(id,pos, name);
    }


    @Override
    synchronized public MyFile getStructure(long id,String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.getFileById(pos,id);
    }

    synchronized public MyFile getStructure(String s) throws NoUserException, NoFileException, ClassNotFoundException, NoAccessException, IOException {
        Disk disk = Disk.constructByUserName(getUserName(s));
        return disk.getStructure(s);

    }

    synchronized public int readByteOfFile(String pos,String name,byte[] buffer,int begin,int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.readByteOfFile(pos,name,buffer,begin,length);
    }

    synchronized public void writeByteOfFile(String pos,String name,long size,String md5,byte[] buffer,int begin,int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeByteOfFile(pos,name,size,md5,buffer,begin,length);
    }

    @Override
    public MyFile getFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(pos);
        MyFile myFile = disk.getStructure(pos);
        if(!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        return myFile.sonFile.get(name);
    }

    @Override
    public MyFile getFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException {
        Disk disk = Disk.constructByUserName(pos);
        MyFile myFile = disk.getStructure(pos);
        if(!myFile.sonDirectory.containsKey(name))
            throw new NoFileException();
        return myFile.sonDirectory.get(name);
    }

    @Override
    public TransDataList getTransDataList(String userName) throws NoSuchUserException {
        return DataBase.getTransDataListByName(userName);
    }

    @Override
    public String getReadOnlyURL(long id, String pos) {
        return "Viewer^0^$"+pos+":"+id;
    }

    @Override
    public String getReadAndWriteURL(long id, String pos) {
        return "Editor^0^$"+pos+":"+id;
    }

    @Override
    public String getTempReadOnlyURL(long id, String pos) {
        return "PreViewer^"+MyDate.getNowTimeStamp()+"^$"+pos+":"+id;
    }

    @Override
    public String getTempReadAndWriteURL(long id, String pos) {
        return "PreEditor^"+MyDate.getNowTimeStamp()+"^$"+pos+":"+id;
    }

    @Override
    public String createReadAndWriteFileURL(String pos, String name) {
        return null;
    }

    @Override
    public String createReadAndWriteFileDirectoryURL(String pos, String name) {
        return null;
    }

    @Override
    public String createReadOnlyFileURL(String pos, String name) {
        return null;
    }

    @Override
    public String createReadOnlyFileDirectoryURL(String pos, String name) {
        return null;
    }

    @Override
    public String createTempReadAndWriteFileURL(String pos, String name) {
        return null;
    }

    @Override
    public String createTempReadAndWriteFileDirectoryURL(String pos, String name) {
        return null;
    }

    @Override
    public String createTempReadOnlyFileURL(String pos, String name) {
        return null;
    }

    @Override
    public String createTempReadOnlyFileDirectoryURL(String pos, String name) throws RemoteException {
        return null;
    }

    @Override
    public void modifyTransDataList(String userName, TransDataList transDataList) throws NoUserException {
        DataBase.setTransDataList(userName,transDataList);
    }


}
