package com.kvrmnks.net;


import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Uploader extends TransLoader implements Runnable, Serializable {
    private static final int PACKAGE_SIZE = 65536*2;
    private long id, curSize, wholeSize;
    private String pos, name, realPos;
    private SimpleLogListProperty simpleLogListProperty;
    private Net server;
    private InfoFile infoFile;
    private File realFile;
    private NetWriter serverWriter;

    public Uploader(long id, String pos, String name, String realPos, SimpleLogListProperty simpleLogListProperty) throws IOException, NotBoundException, NoAccessException, NoUserException, NoFileException, FileExistedException, ClassNotFoundException, FileStructureException {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.simpleLogListProperty = simpleLogListProperty;

    }

    private void init() throws IOException, NotBoundException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, FileStructureException {
        realFile = new File(realPos);
        server = (Net) Naming.lookup(UserData.getServerIp());
        serverWriter = (NetWriter) Naming.lookup(UserData.getWriterIp());
        MyFile myFile = server.getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name)) {
            server.createFile(id, pos, name, realFile.length());
        } else {
            if (myFile.sonFile.get(name).getSize() != realFile.length())
                throw new FileExistedException();
        }
        myFile = server.getStructure(id,pos);
        if (!myFile.sonFile.containsKey(name + ".info")) {
            server.createFile(id, pos, name + ".info");
            infoFile = new InfoFile();
            infoFile.setMd5(MD5.getMD5OfFile(realPos));
            infoFile.setSize(0);
            server.writeInfoFile(id, pos, name+".info", infoFile);
        } else {
            infoFile = server.getInfoFile(id, pos, name + ".info");
            if (!infoFile.getMd5().equals(MD5.getMD5OfFile(realPos)))
                throw new FileExistedException();
            curSize = infoFile.getSize();
        }
        myFile = server.getStructure(id,pos).sonFile.get(name);
        serverWriter.setFile(myFile.getId(),myFile.getName());
        simpleLogListProperty.setProcess((double) curSize / realFile.length());
        wholeSize = realFile.length();
    }

    @Deprecated
    void work() throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(realFile));
        byte[] buffer = new byte[PACKAGE_SIZE];
        MyFile myFile = server.getStructure(id, pos);
        myFile = myFile.sonFile.get(name);
        long curSize = myFile.getSize();
        long wholeSize = realFile.length();
        while (curSize < wholeSize && (!flag)) {
            int tmp = bufferedInputStream.read(buffer, 0, PACKAGE_SIZE);
            server.writeByteOfFile(id, pos, name, buffer, 0, PACKAGE_SIZE);
            curSize += tmp;
            if ((double) curSize / wholeSize > 0.01 + simpleLogListProperty.getProcess())
                simpleLogListProperty.setProcess((double) curSize / wholeSize);
        }
        bufferedInputStream.close();
        if (flag) {
            return;
        }
        simpleLogListProperty.setProcess(1);
        server.deleteFile(id, pos, name + ".info");
    }

    void transfer() throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(realFile.getAbsoluteFile(),"r");
        randomAccessFile.seek(curSize);
        byte[] buffer = new byte[PACKAGE_SIZE];
        long pretime = MyDate.getNowTimeStamp(),preData = curSize;
        while(curSize < wholeSize && (!flag)){
            int tmp = randomAccessFile.read(buffer,0,PACKAGE_SIZE);
            curSize += tmp;
            serverWriter.write(buffer,0,tmp);
            if((double)curSize/wholeSize > 0.01 + simpleLogListProperty.getProcess()){
                simpleLogListProperty.setProcess((double)curSize/wholeSize);
            }
            if(MyDate.getNowTimeStamp() > pretime + 1){
                long delta = MyDate.getNowTimeStamp() - pretime;
                delta = (curSize - preData)/delta;
                simpleLogListProperty.setSpeed(MyFile.transferSize(delta)+"/s");
                pretime = MyDate.getNowTimeStamp();
                preData = curSize;
            }
        }
        if(!flag){
            server.deleteFile(id, pos, name + ".info");
            simpleLogListProperty.setProcess(1);
        }

        randomAccessFile.close();
        serverWriter.close();
    }

    @Override
    public void run() {
        try {
            init();
            transfer();
        } catch (IOException | ClassNotFoundException | NoUserException | NoAccessException | NoFileException | FileStructureException | NotBoundException | FileExistedException e) {
            e.printStackTrace();
        }
    }
}
