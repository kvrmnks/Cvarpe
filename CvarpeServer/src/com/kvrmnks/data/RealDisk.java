package com.kvrmnks.data;

import com.kvrmnks.exception.NoFileException;

import java.io.*;

public class RealDisk {
    public static final String LOCATION = "d:/";


    public static MyFile getFileById(long id) throws NoFileException, IOException, ClassNotFoundException {
        String command = "_" + id + "_" + DataBase.getFile(id) + ".db";
        return (MyFile) new ObjectInputStream(new FileInputStream(new File(command))).readObject();
    }

    synchronized public static void writeMyFile(MyFile myFile, long id, String fileName) throws IOException {
        fileName = "_" + id + "_" + fileName;
        File file = new File(fileName);
        if (!file.exists())
            file.createNewFile();
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
        obj.writeObject(myFile);
        obj.close();
    }

    synchronized public static void deleteFile(MyFile myfile) {
        String command = Disk.LOCATION + "_" + myfile.getId() + "_" + myfile.getName();
        File file = new File(command);
        file.delete();
    }

    synchronized public static void deleteFileDirectory(MyFile myfile) {
        for (MyFile file : myfile.sonFile.values()) {
            deleteFile(file);
        }
        for (MyFile file : myfile.sonDirectory.values()) {
            deleteFileDirectory(file);
        }
    }

    synchronized public static int readByteOfFile(MyFile myFile,byte[] buffer,long begin,int length) throws IOException {
        File file = new File(Disk.LOCATION + "_" + myFile.getId() + "_" + myFile.getName());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsoluteFile(),"r");
        randomAccessFile.seek(begin);
        int tmp = randomAccessFile.read(buffer,0,length);
        randomAccessFile.close();
        return tmp;
    }

    synchronized public static void deleteFile(long id) throws IOException, NoFileException, ClassNotFoundException {
        deleteFile(getFileById(id));
    }

    synchronized public static void deleteFileDirectory(long id) throws IOException, NoFileException, ClassNotFoundException {
        deleteFileDirectory(getFileById(id));
    }

    synchronized public static void renameFile(long id, MyFile myFile) throws NoFileException {
        String command = Disk.LOCATION + "_" + id + "_" + DataBase.getFile(id);
        File file = new File(command);
        File to = new File(Disk.LOCATION + "_" + id + "_" + myFile.getName());
        file.renameTo(to);
    }
}
