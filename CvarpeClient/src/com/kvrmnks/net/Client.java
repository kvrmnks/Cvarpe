package com.kvrmnks.net;

import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class Client {
    //  private static ObjectInputStream socketIn;
    //private static ObjectOutputStream socketOut;
    //   private static Socket socket;
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

    public static MyFile getStructure(String location) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        MyFile myFile = server.getStructure(location);
        myFile.mainTain();
        myFile.buildPath();
        return myFile;
    }

    public static MyFile getStructure(long id, String pos) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        MyFile myFile = server.getStructure(id,pos);
        myFile.mainTain();
        myFile.buildPath();
        return myFile;
    }

    public static void downLoad(String userName, String pos, String name, File location, String ip, SimpleLogListProperty simpleLogListProperty) throws IOException {
        /*TransData transData = new TransData(userName, pos, name, location.getAbsolutePath(), TransData.TYPE_DOWNLOAD);

        TransDataList tmp = new TransDataList(userName);
        try {
            tmp = DataBase.getTransDataListByName(userName);
        } catch (NoSuchUserException e) {}
        tmp.addTranData(transData);
        DataBase.saveData();

        socketOut.writeUTF("DownloadFile$" + pos + "$" + name);
        socketOut.flush();
        if(!socketIn.readBoolean())
            return;
        int port = socketIn.readInt();
        DownLoader downLoader = new DownLoader(transData,ip,port,simpleLogListProperty);
        simpleLogListProperty.setDownLoader(downLoader);
        Thread t = new Thread(downLoader);
        t.start();

         */
    }

    /*
     * 可以指定上传到服务器的文件名的上传
     * fileDirectory 上传到服务器的文件路径
     * fileName 上传到服务器的文件名
     * localFile 待上传的本地文件
     * ip 服务器的地址
     * */
    private static void upload(String userName, String pos, String name, File realPos, String ip
            , SimpleLogListProperty logListProperty) throws IOException {
       /* TransData transData = new TransData(userName, pos, name, realPos.getAbsolutePath(), TransData.TYPE_UPLOAD);

        TransDataList tmp = new TransDataList(userName);
        try {
            tmp = DataBase.getTransDataListByName(userName);
        } catch (NoSuchUserException e) {}
        tmp.addTranData(transData);
        DataBase.saveData();

        socketOut.writeUTF("UploadFile$" + pos + "$" + name);
        socketOut.flush();
        if(!socketIn.readBoolean())
            return;
        int port = socketIn.readInt();
        Uploader uploader = new Uploader(transData, ip, port, logListProperty);
        logListProperty.setUploader(uploader);
        Thread t = new Thread(uploader);
        t.start();

        */
    }

    /*
     * 不可以指定上传到服务器的文件名的上传
     * */
    public static void upload(String userName, String fileDirectory, File localFile, String ip, SimpleLogListProperty loglist) throws IOException {
        //  upload(userName, fileDirectory, localFile.getName(), localFile, ip, loglist);
    }

    /*
        public static void reName(String file,String name,String newName) throws IOException {
            socketOut.writeUTF("Rename" + "$" + file + "$" +name +"$" + newName);
            socketOut.flush();
        }
    */
    public static void deleteFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        server.deleteFile(pos, name);
    }

    public static void deleteFile(long id,String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        server.deleteFile(id,pos, name);
    }
    public static void deleteFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        server.deleteFileDirectory(pos, name);
    }
    public static void deleteFileDirectory(long id,String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        server.deleteFileDirectory(id,pos, name);
    }
    /*
     * fileDirectory 要创建文件夹的目录
     * fileName 要创建的文件夹的名称
     * */
    public static void createFileDirectory(String fileDirectory, String fileName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.createDirectory(fileDirectory, fileName);
    }
    public static void createFileDirectory(long id,String fileDirectory, String fileName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.createDirectory(id,fileDirectory, fileName);
    }

    /*
     * searchFile 搜索文件
     * fileName 为要搜索的文件子串
     * */
    public static MyFile[] searchFile(String fileName) throws IOException, ClassNotFoundException, NoFileException, NoAccessException, NoUserException {
        MyFile full = getStructure("Editor^0^/" + UserData.getUserName() + "/");
        return full.search(fileName);
    }

    /*
     * logup 登录
     * name 用户名
     * password 密码
     * 会自动把输入的密码经过MD5加密之后发送
     * */
    public static boolean logUp(String name, String password) throws IOException, UserExistedException {
        return server.logUp(name, MD5.getMD5(password));
    }

    public static boolean logIn(String name, String password) throws IOException {
        return server.logIn(name, MD5.getMD5(password));
    }

    /*
     * 下载文件夹
     * fileLocation 代表文件夹本地地址
     * panLocation 网盘文件夹位置
     * */
    public static SimpleLogListProperty[] downloadFileDirectory(
            String fileLocation
            , String panLocation
            , String panFileDirectoryName
    ) throws IOException, ClassNotFoundException {
        return null;
        /*
        FileDirectoryDownLoader fileDirectoryDownLoader = new FileDirectoryDownLoader(
                fileLocation
                , panLocation
                , panFileDirectoryName
                , socketIn
                , socketOut
                , serverIp);
        fileDirectoryDownLoader.init();
        fileDirectoryDownLoader.download();
        return fileDirectoryDownLoader.getProperty();

         */
    }

    public static SimpleLogListProperty[] uploadFileDirectory(
            String fileLocation
            , String panLocation
    ) throws IOException {
        return null;
        /*
        FileDirectoryUploader fileDirectoryUploader = new FileDirectoryUploader(
                fileLocation
                , panLocation
                , serverIp
                , socketIn
                , socketOut
        );
        fileDirectoryUploader.init();
        fileDirectoryUploader.upload();
        return fileDirectoryUploader.getProperties();

         */
    }

    public static void reNameFileDirectory(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.renameFileDirectory(pos, name, newName);
    }

    public static void reNameFileDirectory(long id, String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.renameFileDirectory(id, pos, name, newName);
    }

    public static void reNameFile(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.renameFile(pos, name, newName);
    }

    public static void reNameFile(long id, String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException {
        server.renameFile(id, pos, name, newName);
    }

    public static String getReadAndWriteURL(long id,String pos) throws RemoteException {
        return server.getReadAndWriteURL(id,pos);
    }

    public static String getReadOnlyURL(long id,String pos) throws RemoteException {
        return server.getReadOnlyURL(id,pos);
    }

    public static String getTempReadAndWriteURL(long id,String pos) throws RemoteException {
        return server.getTempReadAndWriteURL(id,pos);
    }

    public static String getTempReadOnlyURL(long id,String pos) throws RemoteException {
        return server.getTempReadOnlyURL(id,pos);
    }
}
