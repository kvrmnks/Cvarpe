package com.kvrmnks.data;

import com.kvrmnks.exception.*;

import java.io.*;
import java.util.Scanner;

public class Disk {
    public static final String LOCATION = "d:/";
    private static final long TIME_LIMIT = 3600 * 24 * 3;
    private UserDisk userDisk;
    private String userName;

    private Disk() {
    }

    synchronized public static Disk constructByUserName(String userName) throws ClassNotFoundException, NoUserException, NoFileException, IOException {
        Disk ret = new Disk();
        ret.userName = userName;
        ret.userDisk = UserDisk.getUserDiskByName(userName);
        return ret;
    }

    synchronized public static Disk constructByFileId(long fileID) throws IOException, NoFileException, ClassNotFoundException {
        Disk ret = new Disk();
        ret.userDisk = UserDisk.getUserDiskByFileId(fileID);
        return ret;
    }

    synchronized private boolean canRead(String type, String time) {
        if (type.equals("Editor") || type.equals("Viewer")) {
            return true;
        } else if (MyDate.getNowTimeStamp() - Long.parseLong(time) <= TIME_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    synchronized private boolean canWrite(String type, String time) {
        if (type.equals("Editor")
                || (type.equals("PreEditor") && MyDate.getNowTimeStamp() - Long.parseLong(time) <= TIME_LIMIT)) {
            return true;
        } else {
            return false;
        }
    }

    synchronized private boolean canRead(String pos) {
        String[] str = pos.split("\\^");
        return canRead(str[0], str[1]);
    }

    synchronized private boolean canWrite(String pos) {
        String[] str = pos.split("\\^");
        return canWrite(str[0], str[1]);
    }

    synchronized private String getRealPos(String pos) {
        String[] str = pos.split("\\^");
        return str[2];
    }

    synchronized public void mainTain() throws IOException {
        MyFile file = userDisk.getRoot();
        RealDisk.writeMyFile(file, file.getId(), file.getName() + ".db");
    }

    synchronized public long getFileId(String pos, String name) throws NoFileException, NoAccessException {
        if (!canRead(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        return userDisk.getFileId(pos, name);
    }

    synchronized public long getFileDirectoryId(String pos, String name) throws NoFileException, NoAccessException {
        if (!canRead(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        return userDisk.getFileDirectoryId(pos, name);
    }

    synchronized public void createFileDirectory(String pos, String name) throws NoFileException, IOException, FileExistedException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile mf = userDisk.createFileDirectory(pos, name);
        mf = mf.sonDirectory.get(name);
        mf.setModifyTime(MyDate.getCurTime());
        DataBase.addFile(mf);
        mainTain();
    }

    synchronized public void createFileDirectory(long id, String pos, String name) throws NoAccessException, FileExistedException, NoFileException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        //pos = getRealPos(pos);
        MyFile mf = userDisk.createFileDirectory(id, name);
        //mf = mf.sonDirectory.get(name);
        mf = mf.sonDirectory.get(name);
        mf.setModifyTime(MyDate.getCurTime());
        DataBase.addFile(mf);
        mainTain();
    }

    synchronized public void deleteFileDirectory(String pos, String name) throws NoFileException, IOException, ClassNotFoundException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile file = userDisk.deleteFileDirectory(pos, name);
        MyFile[] myFiles = file.toArray();
        for (MyFile myFile : myFiles) {
            DataBase.deleteFile(myFile.getId());
        }
        mainTain();
    }

    synchronized public void deleteFileDirectory(long id, String pos, String name) throws NoFileException, NoAccessException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile file = userDisk.deleteFileDirectory(id, name);
        MyFile[] myFiles = file.toArray();
        for (MyFile myFile : myFiles) {
            DataBase.deleteFile(myFile.getId());
        }
        mainTain();
    }

    synchronized public void deleteFile(String pos, String name) throws NoFileException, IOException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile mf = userDisk.deleteFile(pos, name);
        DataBase.deleteFile(mf.getId());
        RealDisk.deleteFile(mf);
        mainTain();
    }

    synchronized public void deleteFile(long id, String pos, String name) throws NoFileException, NoAccessException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile mf = userDisk.deleteFile(id, name);
        DataBase.deleteFile(mf.getId());
        RealDisk.deleteFile(mf);
        mainTain();
    }

    synchronized public void renameFile(String pos, String name, String newName) throws NoFileException, FileExistedException, IOException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        long id = userDisk.getFileId(pos, name);
        try {
            userDisk.getFileId(pos, newName);
            throw new FileExistedException();
        } catch (NoFileException e) {
            MyFile file = userDisk.renameFile(pos, name, newName);
            file.sonFile.get(newName).setName(newName);
            RealDisk.renameFile(id, file.sonFile.get(newName));
            DataBase.deleteFile(id);
            DataBase.addFile(file.sonFile.get(newName));
            mainTain();
        }
    }

    synchronized public void renameFile(long id, String pos, String name, String newName) throws FileExistedException, NoFileException, NoAccessException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        MyFile file = userDisk.renameFile(id, name, newName);
        file.sonFile.get(newName).setName(newName);
        RealDisk.renameFile(id, file.sonFile.get(newName));
        DataBase.deleteFile(file.sonFile.get(newName).getId());
        DataBase.addFile(file.sonFile.get(newName));
        mainTain();
    }

    synchronized public void renameFileDirectory(String pos, String name, String newName) throws NoFileException, FileExistedException, IOException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        long id = userDisk.getFileDirectoryId(pos, name);
        try {
            userDisk.getFileDirectoryId(pos, newName);
            throw new FileExistedException();
        } catch (NoFileException e) {
            MyFile file = userDisk.renameFileDirectory(pos, name, newName);
            DataBase.deleteFile(id);
            file.sonDirectory.get(newName).setName(newName);
            file.sonDirectory.get(newName).setModifyTime(MyDate.getCurTime());
            DataBase.addFile(file.sonDirectory.get(newName));
            mainTain();
        }
    }

    synchronized public void renameFileDirectory(long id, String pos, String name, String newName) throws NoFileException, FileExistedException, IOException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        MyFile file = userDisk.renameFileDirectory(id, name, newName);
        DataBase.deleteFile(file.sonDirectory.get(newName).getId());
        file.sonDirectory.get(newName).setName(newName);
        file.sonDirectory.get(newName).setModifyTime(MyDate.getCurTime());
        DataBase.addFile(file.sonDirectory.get(newName));
        mainTain();
    }

    synchronized public void createFile(String pos, String name) throws FileExistedException, NoFileException, IOException, NoAccessException {
        if (!canWrite(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        MyFile my = userDisk.createFile(pos, name);
        my.sonFile.get(name).setModifyTime(MyDate.getCurTime());
        DataBase.addFile(my.sonFile.get(name));
        File file = new File(RealDisk.LOCATION + "_" + my.getId() + "_" + my.getName());
        file.createNewFile();
        mainTain();
    }

    synchronized public void createFile(long id, String pos, String name) throws FileExistedException, NoFileException, NoAccessException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        // pos = getRealPos(pos);
        MyFile my = userDisk.createFile(id, name);
        my.sonFile.get(name).setModifyTime(MyDate.getCurTime());
        DataBase.addFile(my.sonFile.get(name));
        my = my.sonFile.get(name);
        File file = new File(RealDisk.LOCATION + "_" + my.getId() + "_" + my.getName());
        file.createNewFile();
        mainTain();
    }

    synchronized public MyFile getStructure(String pos) throws NoFileException, NoAccessException {
        if (!canRead(pos))
            throw new NoAccessException();
        pos = getRealPos(pos);
        return userDisk.getStructure(pos);
    }

    synchronized public MyFile getStructure(long id, String pos) throws NoAccessException, NoFileException {
        if (!canRead(pos))
            throw new NoAccessException();
        // pos = getRealPos(pos);
        return userDisk.getStructure(id);
    }

    synchronized public String getUserName() {
        return userName;
    }

    synchronized public int readByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws NoAccessException, NoFileException, IOException {
        if (!canRead(pos))
            throw new NoAccessException();
        MyFile myFile = getStructure(id, pos);
        myFile = myFile.sonFile.get(name);
        return RealDisk.readByteOfFile(myFile, buffer, begin, begin + length);
    }

    synchronized public void writeByteOfFile(long id, String pos, String name, byte[] buffer, int begin, int length) throws NoAccessException, NoFileException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        MyFile myFile = getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        myFile = myFile.sonFile.get(name);
        File file = new File(RealDisk.LOCATION + "_" + myFile.getId() + "_" + myFile.getName());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file,true));
        bufferedOutputStream.write(buffer, begin, begin + length);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        myFile.setSize(myFile.getSize() + length);
        mainTain();
    }

    @Deprecated
    synchronized public void writeByteOfFile(String pos, String name, long size, String Md5, byte[] buffer, int begin, int length) throws NoAccessException, NoFileException, FileExistedException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        MyFile file = getStructure(pos);
        if (file.sonFile.containsKey(name)) {
            if (file.sonFile.containsKey(name + ".info")) {

                long infoId = file.sonFile.get(name + ".info").getId();
                Scanner scan = new Scanner(new File(RealDisk.LOCATION + "_" + infoId + "_" + file.sonFile.get(name + ".info").getName()));
                long curSize = scan.nextLong();
                if (size != curSize)
                    throw new FileExistedException();
                String Q = scan.next();
                if (!Q.equals(Md5))
                    throw new FileExistedException();
            } else {
                throw new FileExistedException();
            }
        } else {
            createFile(pos, name);
            createFile(pos, name + ".info");
            long infoId = file.sonFile.get(name + ".info").getId();
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(RealDisk.LOCATION + "_" + infoId + "_" + file.sonFile.get(name + ".info").getName()))));
            pw.println(size);
            pw.println(Md5);
        }
        long realId = file.sonFile.get(name).getId();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(RealDisk.LOCATION + "_" + realId + "_" + file.sonFile.get(name).getName())));
        bufferedOutputStream.write(buffer, begin, length);
        bufferedOutputStream.close();
    }

    synchronized public InfoFile getInfoFile(long id, String pos, String name) throws NoAccessException, NoFileException, IOException, FileStructureException {
        if (!canRead(pos))
            throw new NoAccessException();
        MyFile myFile = getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        myFile = myFile.sonFile.get(name);
        File file = new File(RealDisk.LOCATION + "_" + myFile.getId() + "_" + myFile.getName());
        Scanner scan = new Scanner(file);
        InfoFile infoFile = new InfoFile();
        if (scan.hasNext()) {
            infoFile.setMd5(scan.next());
        } else {
            throw new FileStructureException();
        }
        if (scan.hasNextLong()) {
            infoFile.setSize(scan.nextLong());
        } else {
            throw new FileStructureException();
        }
        mainTain();
        return infoFile;
    }

    public void writeInfoFile(long id, String pos, String name, InfoFile infoFile) throws NoAccessException, NoFileException, IOException {
        if (!canWrite(pos))
            throw new NoAccessException();
        MyFile myFile = getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        myFile = myFile.sonFile.get(name);
        File file = new File(RealDisk.LOCATION + "_" + myFile.getId() + "_" + myFile.getName());
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        pw.println(infoFile.getMd5());
        pw.println(infoFile.getSize());
        pw.flush();
        pw.close();
        mainTain();
    }

    @Deprecated
    synchronized public void println(long id, String pos, String name, String content) throws NoAccessException, NoFileException, IOException {
        MyFile myFile = getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name))
            throw new NoFileException();
        myFile = myFile.sonFile.get(name);
        PrintWriter out = new PrintWriter(new FileOutputStream(new File(RealDisk.LOCATION + "_" + myFile.getId() + "_" + myFile.getName())));
        out.println(content);
        out.flush();
        out.close();
        mainTain();
    }

    synchronized public MyFile getFileById(String pos, long id) throws NoAccessException, NoFileException {
        if (!canRead(pos))
            throw new NoAccessException();
        MyFile myFile = userDisk.getRoot();
        MyFile[] myFiles = myFile.toArray();
        for (MyFile mf : myFiles) {
            if (mf.getId() == id)
                return mf;
        }
        throw new NoFileException();
    }


}