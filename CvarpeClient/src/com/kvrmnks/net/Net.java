package com.kvrmnks.net;

import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.TransDataList;
import com.kvrmnks.exception.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Net extends Remote {
    MyFile getStructure(String pos) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException;

    void createDirectory(String pos, String name) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException;

    void deleteFileDirectory(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void deleteFile(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void renameFileDirectory(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void renameFile(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void uploadFile(String pos, String name) throws RemoteException, IOException;

    void downloadFile(String pos, String name) throws RemoteException, IOException;

    boolean logIn(String userName, String userPassword) throws RemoteException, IOException;

    boolean logUp(String userName, String userPassword) throws RemoteException, IOException, UserExistedException;

    int readByteOfFile(String pos, String name, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    MyFile getFile(String pos,String name);

    MyFile getFileDirectory(String pos,String name);

    TransDataList getTransDataList(String userName);
}
