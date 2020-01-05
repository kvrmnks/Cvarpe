package com.kvrmnks.net;

import com.kvrmnks.data.InfoFile;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.exception.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ConcurrentModificationException;

public interface Net extends Remote {

    MyFile getStructure(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    @Deprecated
    MyFile getStructure(String pos) throws IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException, NoSuchUserException;

    void createFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    void createFile(long id, String pos, String name, long size) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    void createFile(long id, String pos, String name, long size, long modifiedTime) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    @Deprecated
    void createDirectory(String pos, String name) throws IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException, NoSuchUserException;

    void createDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    @Deprecated
    void deleteFileDirectory(String pos, String name) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, NoSuchUserException;

    void deleteFileDirectory(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    @Deprecated
    void deleteFile(String pos, String name) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, NoSuchUserException;

    void deleteFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    @Deprecated
    void renameFileDirectory(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException, NoSuchUserException;

    void renameFileDirectory(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    @Deprecated
    void renameFile(String pos, String name, String newName) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException, NoSuchUserException;

    void renameFile(long id, String pos, String name, String newName) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    boolean logIn(String userName, String userPassword) throws IOException, NoSuchUserException;

    boolean logUp(String userName, String userPassword) throws IOException, UserExistedException, NoUserException, NoFileException, ClassNotFoundException, NoSuchUserException;

    int readByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    void writeByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, NoSuchUserException;

    @Deprecated
    void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException, NoSuchUserException;

    InfoFile getInfoFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileStructureException, NoSuchUserException;

    void writeInfoFile(long id, String pos, String name, InfoFile infoFile) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    String getMd5OfFile(long id, String pos, String name) throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException;

    @Deprecated
    MyFile getFile(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    @Deprecated
    MyFile getFileDirectory(String pos, String name) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, NoSuchUserException;

    String getReadOnlyURL(long id, long fatherId, String pos) throws RemoteException;

    String getReadAndWriteURL(long id, long fatherId, String pos) throws RemoteException;

    String getTempReadOnlyURL(long id, long fatherId, String pos) throws RemoteException;

    String getTempReadAndWriteURL(long id, long fatherId, String pos) throws RemoteException;

    String[] getShareListByName(String name) throws RemoteException;

    void addShareList(String name, String content) throws RemoteException;

    void deleteShareList(String name, String content) throws RemoteException;

    long getUsedCapacity(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoSuchUserException;

    long getCapacity(long id, String pos) throws ClassNotFoundException, NoUserException, NoFileException, IOException, NoSuchUserException;

}
