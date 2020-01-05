package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import javafx.application.Platform;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server extends UnicastRemoteObject implements Net {

    private MainController mainController;

    synchronized private String getUserName(String pos) {
        return pos.split("/")[1];
    }

    synchronized private void mainTain(String userName, long size) {
        Platform.runLater(() -> {
            try {
                DataBase.modifyUserUsedCapacity(userName,size);
            } catch (NoSuchUserException e) {
                e.printStackTrace();
            }finally {
                mainController.flushUserTable();
            }
        });
    }

    public Server(MainController mainController) throws RemoteException {
        super();
        this.mainController = mainController;
    }

    synchronized public boolean logUp(String userName, String userPassword) throws IOException, UserExistedException, NoSuchUserException {
        DataBase.addUser(new User(userName, userPassword,MyDate.getRealTimeStamp()));
        Platform.runLater(mainController::flushUserTable);
        Log.log("注册账户\n姓名:" + userName + " 密码:" + userPassword);
        Disk disk = Disk.constructByUserName(userName);
        disk.mainTain();
        return true;
    }

    synchronized public boolean logIn(String userName, String userPassword) throws NoSuchUserException {
        if (UserManager.checkUser(userName, userPassword)) {
            Log.log("用户登录:\n姓名:" + userName + " 密码:" + userPassword);
            DataBase.modifyUserLastLogin(userName,MyDate.getRealTimeStamp());
            Platform.runLater(mainController::flushUserTable);
            return true;
        }
        return false;
    }

    @Deprecated
    @Override
    synchronized public void renameFile(String pos, String name, String newName) throws NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(pos, name, newName);
    }

    @Override
    synchronized public void renameFile(long id, String pos, String name, String newName) throws NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFile(id, pos, name, newName);
        Log.log("修改文件名\nid:"+id+"\npassword:"+pos+"\n"+name+" -> "+newName);
    }

    @Deprecated
    @Override
    synchronized public void renameFileDirectory(String pos, String name, String newName) throws NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(pos, name, newName);
        mainTain(disk.getUserName(),disk.getSize());
    }

    @Override
    synchronized public void renameFileDirectory(long id, String pos, String name, String newName) throws NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.renameFileDirectory(id, pos, name, newName);
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("修改文件夹名\nid:"+id+"\npassword:"+pos+"\n"+name+" -> "+newName);
    }

    @Override
    @Deprecated
    synchronized public void deleteFile(String pos, String name) throws NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(pos, name);
        mainTain(disk.getUserName(),disk.getSize());
    }

    @Override
    synchronized public void deleteFile(long id, String pos, String name) throws NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFile(id, pos, name);
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("删除文件\nid:"+id+"\npos:"+pos+"\nname:"+name);
    }

    @Override
    @Deprecated
    synchronized public void deleteFileDirectory(String pos, String name) throws ClassNotFoundException, NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(pos, name);
        mainTain(disk.getUserName(),disk.getSize());
    }

    @Override
    synchronized public void deleteFileDirectory(long id, String pos, String name) throws NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.deleteFileDirectory(id, pos, name);
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("删除文件夹\nid:"+id+"\npos:"+pos+"\nname:"+name);
    }

    @Override
    synchronized public void createFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFile(id, pos, name);
        disk.mainTain();
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("创建文件\n"+"id:"+id+"\npos:"+pos+"\nname:"+name);
    }

    @Override
    synchronized public void createFile(long id, String pos, String name, long size) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));

        SimpleUserProperty ret = null;
        for(SimpleUserProperty x : MainController.data){
            if(x.getName().equals(disk.getUserName())){
                ret = x;
                break;
            }
        }
        if(size + disk.getSize() > ret.getUser().getCapacity()){
            throw new NoAccessException();
        }

        disk.createFile(id, pos, name);
        MyFile myFile = disk.getStructure(id, pos);
        myFile.sonFile.get(name).setSize(size);
        disk.mainTain();
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("创建文件\n"+"id:"+id+"\npos:"+pos+"\nname:"+name+"\nsize:"+size);
    }

    @Override
    synchronized public void createFile(long id, String pos, String name, long size, long modifiedTime) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));

        SimpleUserProperty ret = null;
        for(SimpleUserProperty x : MainController.data){
            if(x.getName().equals(disk.getUserName())){
                ret = x;
                break;
            }
        }
        if(ret == null)return;
        if(size + disk.getSize() > ret.getUser().getCapacity()){
            throw new NoAccessException();
        }

        disk.createFile(id, pos, name);
        MyFile myFile = disk.getStructure(id, pos);
        myFile.sonFile.get(name).setSize(size);
        myFile.sonFile.get(name).setModifyTime(MyDate.convert("" + modifiedTime));
        disk.mainTain();
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("创建文件\n"+"id:"+id+"\npos:"+pos+"\nname:"+name+"\nsize:"+size+"\nmodifyTime"+modifiedTime);
    }

    @Deprecated
    @Override
    synchronized public void createDirectory(String pos, String name) throws NoFileException, NoAccessException, FileExistedException, IOException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(pos, name);
        mainTain(disk.getUserName(),disk.getSize());

    }

    @Override
    synchronized public void createDirectory(long id, String pos, String name) throws NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.createFileDirectory(id, pos, name);
        mainTain(disk.getUserName(),disk.getSize());
        Log.log("创建文件夹:\nid:"+id+"\npos:"+pos+"\nname:"+name);
    }

    @Override
    synchronized public MyFile getStructure(long id, String pos) throws NoFileException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        MyFile myFile = disk.getFileById(pos, id);
        Log.log("访问文件:\nid"+id+"\npos:"+pos);
        return myFile;
    }

    @Override
    synchronized public MyFile getStructure(String s) throws NoFileException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(s));
        MyFile myFile = disk.getStructure(s);
        Log.log("访问文件:\npos"+s);
        return myFile;
    }

    @Deprecated
    @Override
    synchronized public int readByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        return disk.readByteOfFile(id, pos, name, buffer, begin, length);
    }

    @Deprecated
    @Override
    synchronized public void writeByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeByteOfFile(id, pos, name, buffer, begin, length);
    }

    @Override
    @Deprecated
    synchronized public void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeByteOfFile(pos, name, size, md5, buffer, begin, length);
    }

    @Override
    synchronized public InfoFile getInfoFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileStructureException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        InfoFile ret = disk.getInfoFile(id, pos, name);
        Log.log("得到info文件\nid:"+id+"\npos:"+pos+"\nname:"+name);
        return ret;
    }

    @Override
    synchronized public void writeInfoFile(long id, String pos, String name, InfoFile infoFile) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        disk.writeInfoFile(id, pos, name, infoFile);
        Log.log("写info文件\nid:"+id+"\npos:"+pos+"\nname:"+name);
    }

    @Deprecated
    @Override
    synchronized public String getMd5OfFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
        MyFile file = getStructure(id, pos);
        if (!file.sonFile.containsKey(name))
            throw new NoFileException();
        file = file.sonFile.get(name);
        return MD5.getMD5OfFile(Disk.LOCATION + "__" + file.getId() + "__" + file.getName());
    }

    @Override
    synchronized public MyFile getFile(String pos, String name) throws NoFileException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        MyFile myFile = disk.getStructure(pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        return myFile.sonFile.get(name);
    }

    @Override
    synchronized public MyFile getFileDirectory(String pos, String name) throws NoFileException, NoAccessException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        MyFile myFile = disk.getStructure(pos);
        if (!myFile.sonDirectory.containsKey(name))
            throw new NoFileException();
        return myFile.sonDirectory.get(name);
    }

    @Override
    synchronized public String getReadOnlyURL(long id, long fatherId, String pos) {
        Log.log("创建只读永久链接\nid:"+id+"\nfatherId:"+fatherId+"\npos:"+pos);
        return "Viewer^0^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getReadAndWriteURL(long id, long fatherId, String pos) {
        Log.log("创建可读写永久链接\nid:"+id+"\nfatherId:"+fatherId+"\npos:"+pos);
        return "Editor^0^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getTempReadOnlyURL(long id, long fatherId, String pos) {
        Log.log("创建只读临时链接\nid:"+id+"\nfatherId:"+fatherId+"\npos:"+pos);
        return "PreViewer^" + MyDate.getNowTimeStamp() + "^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    synchronized public String getTempReadAndWriteURL(long id, long fatherId, String pos) {
        Log.log("创建可读写临时链接\nid:"+id+"\nfatherId:"+fatherId+"\npos:"+pos);
        return "PreEditor^" + MyDate.getNowTimeStamp() + "^$" + pos + ":" + id + ":" + fatherId;
    }

    @Override
    public String[] getShareListByName(String name) throws RemoteException {
        Log.log("获取分享列表:\nname:"+name);
        return DataBase.getShareList(name);
    }

    @Override
    public void addShareList(String name, String content) throws RemoteException {
        Log.log("添加分享链接:\nname:"+name+"\ncontent:"+content);
        DataBase.addShareList(name, content);
    }

    @Override
    public void deleteShareList(String name, String content) throws RemoteException {
        Log.log("删除分享链接:\nname:"+name+"\ncontent:"+content);
        DataBase.deleteShareList(name, content);
    }

    @Override
    public long getUsedCapacity(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        long ret = DataBase.getUser(disk.getUserName()).getUsedCapacity();
        Log.log("得到已用空间大小\nid:"+id+"\npos:"+pos);
        return ret;
    }

    @Override
    public long getCapacity(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoSuchUserException {
        Disk disk = Disk.constructByUserName(getUserName(pos));
        long ret = DataBase.getUser(disk.getUserName()).getCapacity();
        Log.log("得到空间大小\nid:"+id+"\npos:"+pos);
        return ret;
    }


}
