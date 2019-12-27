package com.kvrmnks.net;

import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.TransDataList;
import com.kvrmnks.exception.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Net extends Remote {
    MyFile getStructure(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    MyFile getStructure(String pos) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException;

    void createDirectory(String pos, String name) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException;

    void createDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    void deleteFileDirectory(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void deleteFileDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    void deleteFile(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void deleteFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    void renameFileDirectory(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void renameFileDirectory(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    void renameFile(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void renameFile(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    void uploadFile(String pos, String name) throws RemoteException, IOException;

    void downloadFile(String pos, String name) throws RemoteException, IOException;

    boolean logIn(String userName, String userPassword) throws RemoteException, IOException;

    boolean logUp(String userName, String userPassword) throws RemoteException, IOException, UserExistedException;

    int readByteOfFile(String pos, String name, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    MyFile getFile(String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    MyFile getFileDirectory(String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    TransDataList getTransDataList(String userName) throws RemoteException, NoSuchUserException;

    String getReadOnlyURL(long id,String pos) throws RemoteException;

    String getReadAndWriteURL(long id,String pos) throws RemoteException;

    String getTempReadOnlyURL(long id,String pos)throws RemoteException;

    String getTempReadAndWriteURL(long id,String pos)throws RemoteException;

    String createReadAndWriteFileURL(String pos, String name) throws RemoteException;

    String createReadAndWriteFileDirectoryURL(String pos, String name) throws RemoteException;

    String createReadOnlyFileURL(String pos, String name) throws RemoteException;

    String createReadOnlyFileDirectoryURL(String pos, String name) throws RemoteException;

    String createTempReadAndWriteFileURL(String pos, String name) throws RemoteException;

    String createTempReadAndWriteFileDirectoryURL(String pos, String name) throws RemoteException;

    String createTempReadOnlyFileURL(String pos, String name) throws RemoteException;

    String createTempReadOnlyFileDirectoryURL(String pos, String name) throws RemoteException;

    void modifyTransDataList(String userName, TransDataList transDataList) throws RemoteException, NoUserException;
}
