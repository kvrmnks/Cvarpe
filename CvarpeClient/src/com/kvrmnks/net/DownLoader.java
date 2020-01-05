package com.kvrmnks.net;

import com.kvrmnks.data.MyDate;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.*;
import javafx.application.Platform;
import sun.awt.PlatformFont;
import sun.java2d.pipe.BufferedPaints;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class DownLoader extends TransLoader implements Runnable, Serializable {
    private static final int PACKAGE_SIZE = 65536*2;
    private long id;
    private String pos, realPos, modifyTime, name;
    //private boolean flag = false;
    private SimpleLogListProperty simpleLogListProperty;
    private Net server;
    private File realFile, infoFile;
    private long curSize, wholeSize;
    private NetReader serverReader;
    private boolean isEqual = true;
    private long heart_id;

    public DownLoader(long id, String pos, String name, String realPos, SimpleLogListProperty simpleLogListProperty) {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.simpleLogListProperty = simpleLogListProperty;
        modifyTime = "";
    }

    public void init() throws IOException, NotBoundException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NoSuchUserException {
        server = UserData.server;
        serverReader = UserData.serverReader;
        buildFile();

        MyFile myFile = server.getStructure(id, pos);
        myFile = myFile.sonFile.get(name);

        if(!modifyTime.equals("")){
            if((!modifyTime.equals(myFile.getModifyTime())) || (wholeSize != myFile.getSize())){
                isEqual = false;
                return;
            }
        }else{
            modifyTime = myFile.getModifyTime();
            wholeSize = myFile.getSize();
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(infoFile));
            printWriter.println(modifyTime);
            printWriter.println(wholeSize);
            printWriter.close();
        }

        wholeSize = myFile.getSize();
        serverReader.setFile(myFile.getId(),myFile.getName());

        heart_id = myFile.getId();

        serverReader.seek(heart_id,curSize);


        simpleLogListProperty.setProcess(((double) curSize) / ((double) myFile.getSize()));
    }

    private void buildFile() throws IOException {
        realFile = new File(realPos);
        infoFile = new File(realPos + ".info");
        System.out.println(realPos + ".info");
        if (!realFile.exists()) realFile.createNewFile();
        else{
            curSize = realFile.length();
        }
        if (!infoFile.exists()) infoFile.createNewFile();
        else {
            Scanner scan = new Scanner(infoFile);
            if (scan.hasNextLine())
                modifyTime = scan.nextLine();
            if (scan.hasNextLong())
                wholeSize = scan.nextLong();
        }
    }

    @Deprecated
    private void work(){
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(realFile, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        byte[] buffer = new byte[PACKAGE_SIZE];
        int tmp = 0;
        while (curSize < wholeSize && (!flag)) {
            try {
                tmp = server.readByteOfFile(id, pos, name, buffer, 0, PACKAGE_SIZE);
                bufferedOutputStream.write(buffer, 0, tmp);
                curSize += tmp;

                if ((double) curSize / wholeSize > simpleLogListProperty.getProcess() + 0.01) {
                    simpleLogListProperty.setProcess((double) curSize / wholeSize);
                }

            } catch (ClassNotFoundException | NoUserException | NoFileException | IOException | NoAccessException | NoSuchUserException e) {
                e.printStackTrace();
                return;
            }

        }
        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag) {
            return;
        }
        simpleLogListProperty.setProcess(1);
        infoFile.delete();
    }

    private void transfer() throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(realFile,true));

        long pretime = MyDate.getNowTimeStamp();
        long preData = curSize;

        while(curSize < wholeSize && (!flag)){
            byte[] buffer  = serverReader.read(heart_id,0,PACKAGE_SIZE);

            int tmp = serverReader.getSize(heart_id);
            dataOutputStream.write(buffer,0,tmp);

            curSize += tmp;
            if ((double) curSize / wholeSize > simpleLogListProperty.getProcess() + 0.01) {
                simpleLogListProperty.setProcess((double) curSize / wholeSize);
            }
            if(MyDate.getNowTimeStamp() - pretime >= 1){
                long delta = MyDate.getNowTimeStamp() - pretime;
                delta = (curSize - preData)/delta;
                simpleLogListProperty.setSpeed(MyFile.transferSize(delta)+"/s");
                pretime = MyDate.getNowTimeStamp();
                preData = curSize;
            }
        }
        if(curSize >= wholeSize){
            infoFile.delete();
            simpleLogListProperty.setProcess(1);
            simpleLogListProperty.setFinished(true);
            simpleLogListProperty.setCondition("完成");
        }
        dataOutputStream.close();
        serverReader.close(heart_id);

    }

    @Override
    public void run() {
        try {
           // Log.log("begin");
            init();
            if(isEqual)
                transfer();
          //  Log.log("inter");
            Platform.runLater(
                    ()->{
                        try {
                            this.mainController.flush();
                            this.mainController.shareFlush();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException | NotBoundException | NoUserException | NoFileException | NoAccessException | ClassNotFoundException | NoSuchUserException e) {
            e.printStackTrace();
        }
    }
}
