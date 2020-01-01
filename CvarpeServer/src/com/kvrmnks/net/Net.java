package com.kvrmnks.net;

import com.kvrmnks.data.InfoFile;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.exception.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Net extends Remote {

    MyFile getStructure(long id, String pos) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    @Deprecated
    MyFile getStructure(String pos) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException;

    void createFile(long id, String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    void createFile(long id, String pos, String name, long size) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    void createFile(long id,String pos,String name,long size,long modifiedTime) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    @Deprecated
    void createDirectory(String pos, String name) throws RemoteException, IOException, NoUserException, NoFileException, ClassNotFoundException, NoAccessException, FileExistedException;

    void createDirectory(long id, String pos, String name) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    @Deprecated
    void deleteFileDirectory(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void deleteFileDirectory(long id, String pos, String name) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    @Deprecated
    void deleteFile(String pos, String name) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    void deleteFile(long id, String pos, String name) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    @Deprecated
    void renameFileDirectory(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void renameFileDirectory(long id, String pos, String name, String newName) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    @Deprecated
    void renameFile(String pos, String name, String newName) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException, FileExistedException;

    void renameFile(long id, String pos, String name, String newName) throws RemoteException,ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    boolean logIn(String userName, String userPassword) throws RemoteException, IOException;

    boolean logUp(String userName, String userPassword) throws RemoteException, IOException, UserExistedException;

    int readByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    void writeByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws RemoteException, IOException, ClassNotFoundException, NoUserException, NoFileException, NoAccessException;

    @Deprecated
    void writeByteOfFile(String pos, String name, long size, String md5, byte[] buffer, int begin, int length) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileExistedException;

    InfoFile getInfoFile(long id, String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException, FileStructureException;

    void writeInfoFile(long id, String pos, String name, InfoFile infoFile) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    String getMd5OfFile(long id, String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException;

    @Deprecated
    MyFile getFile(String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    @Deprecated
    MyFile getFileDirectory(String pos, String name) throws RemoteException, ClassNotFoundException, NoUserException, NoFileException, IOException, NoAccessException;

    String getReadOnlyURL(long id, long fatherId, String pos) throws RemoteException;

    String getReadAndWriteURL(long id, long fatherId, String pos) throws RemoteException;

    String getTempReadOnlyURL(long id, long fatherId, String pos) throws RemoteException;

    String getTempReadAndWriteURL(long id, long fatherId, String pos) throws RemoteException;

}
