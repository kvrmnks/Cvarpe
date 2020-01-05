package com.kvrmnks.net;

import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private static String serverIp;
    private static int port;
    public static Net server;

    public static void setPort(int port) {
        Client.port = port;
    }

    public static void setIp(String ip) {
        serverIp = ip;
    }

    public static void setNet(Net net) {
        server = net;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        Client.serverIp = serverIp;
    }

    @Deprecated
    public static MyFile getStructure(String location) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        MyFile myFile = server.getStructure(location);
        myFile.mainTain();
        myFile.buildPath();
        return myFile;
    }

    public static MyFile getStructure(long id, String pos) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        MyFile myFile = server.getStructure(id, pos);
        myFile.mainTain();
        myFile.buildPath();
        return myFile;
    }
@Deprecated
    public static void downLoad(long id, String pos, String name, String realPos, SimpleLogListProperty simpleLogListProperty) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NotBoundException {
        DownLoader downLoader = new DownLoader(id, pos, name, realPos, simpleLogListProperty);
        new Thread(downLoader).start();
    }

    /*
     * 可以指定上传到服务器的文件名的上传
     * fileDirectory 上传到服务器的文件路径
     * fileName 上传到服务器的文件名
     * localFile 待上传的本地文件
     * ip 服务器的地址
     * */
    @Deprecated
    public static void upload(long id,String pos,String name,String realPos,SimpleLogListProperty simpleLogListProperty) throws IOException, NoUserException, NoAccessException, NoFileException, ClassNotFoundException, FileStructureException, NotBoundException, FileExistedException {
        Uploader uploader = new Uploader(id,pos,name,realPos,simpleLogListProperty);
        new Thread(uploader).start();
    }

    /*
     * 不可以指定上传到服务器的文件名的上传
     * */
    @Deprecated
    public static void upload(String userName, String fileDirectory, File localFile, String ip, SimpleLogListProperty loglist) throws IOException {
        //  upload(userName, fileDirectory, localFile.getName(), localFile, ip, loglist);
    }

    /*
        public static void reName(String file,String name,String newName) throws IOException {
            socketOut.writeUTF("Rename" + "$" + file + "$" +name +"$" + newName);
            socketOut.flush();
        }
    */
    @Deprecated
    public static void deleteFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        server.deleteFile(pos, name);
    }

    public static void deleteFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        server.deleteFile(id, pos, name);
    }

    @Deprecated
    public static void deleteFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        server.deleteFileDirectory(pos, name);
    }

    public static void deleteFileDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        server.deleteFileDirectory(id, pos, name);
    }

    @Deprecated
    public static void createFileDirectory(String fileDirectory, String fileName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.createDirectory(fileDirectory, fileName);
    }

    public static void createFileDirectory(long id, String fileDirectory, String fileName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.createDirectory(id, fileDirectory, fileName);
    }

    public static MyFile[] searchFile(String fileName) throws IOException, ClassNotFoundException, NoFileException, NoAccessException, NoUserException, NoSuchUserException {
        MyFile full = getStructure("Editor^0^/" + UserData.getUserName() + "/");
        return full.search(fileName);
    }

    public static boolean logUp(String name, String password) throws IOException, UserExistedException, ClassNotFoundException, NoFileException, NoUserException, NoSuchUserException {
        return server.logUp(name, MD5.getMD5(password));
    }

    public static boolean logIn(String name, String password) throws IOException, NoSuchUserException {
        return server.logIn(name, MD5.getMD5(password));
    }

    public static SimpleLogListProperty[] downloadFileDirectory(
            String fileLocation
            , String panLocation
            , String panFileDirectoryName
    ) throws IOException, ClassNotFoundException {
        return null;
    }

    public static SimpleLogListProperty[] uploadFileDirectory(
            String fileLocation
            , String panLocation
    ) throws IOException {
        return null;

    }

    @Deprecated
    public static void reNameFileDirectory(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.renameFileDirectory(pos, name, newName);
    }

    public static void reNameFileDirectory(long id, String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.renameFileDirectory(id, pos, name, newName);
    }

    @Deprecated
    public static void reNameFile(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.renameFile(pos, name, newName);
    }

    public static void reNameFile(long id, String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, NoSuchUserException {
        server.renameFile(id, pos, name, newName);
    }

    public static String getReadAndWriteURL(long id, long fatherId,String pos) throws RemoteException {
        return server.getReadAndWriteURL(id,fatherId, pos);
    }

    public static String getReadOnlyURL(long id, long fatherId,String pos) throws RemoteException {
        return server.getReadOnlyURL(id,fatherId, pos);
    }

    public static String getTempReadAndWriteURL(long id, long fatherId,String pos) throws RemoteException {
        return server.getTempReadAndWriteURL(id, fatherId,pos);
    }

    public static String getTempReadOnlyURL(long id, long fatherId,String pos) throws RemoteException {
        return server.getTempReadOnlyURL(id,fatherId, pos);
    }

    public static String[] getShareList() throws RemoteException {
        return server.getShareListByName(UserData.getUserName());
    }

    public static void addShareList(String content) throws RemoteException {
        server.addShareList(UserData.getUserName(),content);
    }

    public static void deleteShareList(String content) throws RemoteException {
        server.deleteShareList(UserData.getUserName(),content);
    }

    public static long getCapacity(long id,String pos) throws ClassNotFoundException, NoUserException, NoFileException, NoSuchUserException, IOException {
        return server.getCapacity(id,pos);
    }

    public static long getUsedCapacity(long id,String pos) throws ClassNotFoundException, NoUserException, NoFileException, NoSuchUserException, IOException {
        return server.getUsedCapacity(id,pos);
    }
}
