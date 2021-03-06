package com.kvrmnks.net;


import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import javafx.application.Platform;

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
    private long heart_id;
    public Uploader(long id, String pos, String name, String realPos, SimpleLogListProperty simpleLogListProperty) {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.simpleLogListProperty = simpleLogListProperty;

    }

   synchronized public void init() throws IOException, NotBoundException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, FileExistedException, FileStructureException, NoSuchUserException {
        realFile = new File(realPos);
        server = UserData.server;
        serverWriter = UserData.serverWriter;
        MyFile myFile = server.getStructure(id, pos);
        boolean hasFile = false;
        if (!myFile.sonFile.containsKey(name)) {
            server.createFile(id, pos, name, realFile.length(),realFile.lastModified());
        } else {
           // Log.log(myFile.sonFile.get(name).getModifyTime());
            //Log.log(MyDate.convert(""+new File(realPos).lastModified()));
            if (myFile.sonFile.get(name).getSize() != realFile.length()
                    || (!myFile.sonFile.get(name).getModifyTime().equals(MyDate.convert(""+realFile.lastModified()))))
                throw new FileExistedException();
            hasFile = true;
        }
        myFile = server.getStructure(id,pos);
        if ((!myFile.sonFile.containsKey(name + ".info")) && (hasFile == false)) {
            server.createFile(id, pos, name + ".info");
            infoFile = new InfoFile();
            infoFile.setModifyTime(MyDate.convert(""+new File(realPos).lastModified()));
            infoFile.setSize(0);
            server.writeInfoFile(id, pos, name+".info", infoFile);
        } else if(!myFile.sonFile.containsKey(name + ".info")){
            simpleLogListProperty.setProcess(1);
            simpleLogListProperty.setFinished(true);
            simpleLogListProperty.setCondition("完成");
            throw new FileExistedException();
        } else {
            infoFile = server.getInfoFile(id, pos, name + ".info");
            //   Log.log(infoFile.getModifyTime());
            //   Log.log(MyDate.convert(""+new File(realPos).lastModified()));
            if (myFile.sonFile.get(name).getSize() != new File(realPos).length()
                    || (!infoFile.getModifyTime().equals(MyDate.convert(""+new File(realPos).lastModified()))))
                throw new FileExistedException();
            curSize = infoFile.getSize();
        }
        myFile = server.getStructure(id,pos).sonFile.get(name);
      //  Log.log(name+" "+serverWriter.toString());

        heart_id = myFile.getId();
        serverWriter.setFile(myFile.getId(),myFile.getName());
        simpleLogListProperty.setProcess((double) curSize / realFile.length());
        wholeSize = realFile.length();
    }

    @Deprecated
    void work() throws ClassNotFoundException, NoUserException, NoAccessException, NoFileException, IOException, NoSuchUserException {
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

    synchronized void transfer() throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NoSuchUserException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(realFile.getAbsoluteFile(),"r");
        randomAccessFile.seek(curSize);
        byte[] buffer = new byte[PACKAGE_SIZE];
        long pretime = MyDate.getNowTimeStamp(),preData = curSize;
        while(curSize < wholeSize && (!flag)){
            int tmp = randomAccessFile.read(buffer,0,PACKAGE_SIZE);
            curSize += tmp;
           // Log.log(name+"    %%%% "+realPos);
            serverWriter.write(heart_id,buffer,0,tmp);
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
        infoFile.setSize(curSize);
        server.writeInfoFile(id, pos, name+".info", infoFile);
        if(!flag){
            server.deleteFile(id, pos, name + ".info");
            simpleLogListProperty.setProcess(1);
            simpleLogListProperty.setFinished(true);
            simpleLogListProperty.setCondition("完成");
        }
        infoFile.setSize(curSize);
        randomAccessFile.close();
        serverWriter.close(heart_id);
    }

    @Override
    public void run() {
        try {
            init();
            transfer();
            Platform.runLater(
                    ()->{
                        try {
                            this.mainController.flush();
                            this.mainController.shareFlush();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException | ClassNotFoundException | NoUserException | NoAccessException | NoFileException | FileStructureException | NotBoundException | FileExistedException | NoSuchUserException e) {
            e.printStackTrace();
        }
    }
}
