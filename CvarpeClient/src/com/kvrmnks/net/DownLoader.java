package com.kvrmnks.net;

import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.NoAccessException;
import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoUserException;
import sun.java2d.pipe.BufferedPaints;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class DownLoader implements Runnable, Serializable {
    private static final int PACKAGE_SIZE = 1024;
    private long id;
    private String pos, realPos,md5,name;
    private boolean flag = false;
    private SimpleLogListProperty simpleLogListProperty;
    private Net server;
    private File realFile,infoFile;
    private long curSize,wholeSize;


    public DownLoader(long id, String pos,String name, String realPos, SimpleLogListProperty simpleLogListProperty) throws IOException, NotBoundException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.simpleLogListProperty = simpleLogListProperty;
        md5 = "";
        server = (Net)Naming.lookup(UserData.getIp());
        buildFile();
        MyFile myFile = server.getStructure(id,pos);
        myFile = myFile.sonFile.get(name);
        wholeSize = myFile.getSize();
        simpleLogListProperty.setProcess(((double)curSize)/((double)myFile.getSize()));
    }

    private void buildFile() throws IOException {
        realFile = new File(realPos);
        infoFile = new File(infoFile+".info");
        if(!realFile.exists())realFile.createNewFile();
        if(!infoFile.exists())infoFile.createNewFile();
        else{
            Scanner scan = new Scanner(realFile);
            if(scan.hasNext())
                md5 = scan.next();
            if(scan.hasNextLong())
                wholeSize = scan.nextLong();
        }
    }

    public void stop() {
        flag = true;
    }


    @Override
    public void run() {

        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(realFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        byte[] buffer = new byte[PACKAGE_SIZE];
        int tmp = 0;
        while(curSize < wholeSize && flag){
            try {
                tmp = server.readByteOfFile(id,pos,name,buffer,0,PACKAGE_SIZE);
                bufferedOutputStream.write(buffer,0,tmp);
                curSize += tmp;

                if((double)curSize/wholeSize > simpleLogListProperty.getProcess() + 0.1){
                    simpleLogListProperty.setProcess((double)curSize/wholeSize);
                }

            } catch (ClassNotFoundException | NoUserException | NoFileException | IOException | NoAccessException e) {
                e.printStackTrace();
                return;
            }

        }

        simpleLogListProperty.setProcess(1);
        infoFile.delete();
    }
}
