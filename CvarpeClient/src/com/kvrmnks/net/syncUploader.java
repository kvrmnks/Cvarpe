package com.kvrmnks.net;

import com.kvrmnks.Main;
import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class syncUploader extends sync implements Runnable{
    private MyFile localMyFile, cloudMyFile;
    private long id;
    private String pos;
    private Net server;
    private File localFile;
    private MainController mainController;
    private Vector<SimpleLogListProperty> transData = new Vector<>();

    public boolean check() {
        for (SimpleLogListProperty x : transData) {
            if (!x.isFinished()) {
                this.condition = new SimpleStringProperty("进行中");
                return false;
            }
        }
        this.condition = new SimpleStringProperty("完成");
        return true;
    }

    private void fileToMyFile(File file, MyFile myFile) {
        if (file.isFile()) return;
        File[] files = file.listFiles();
        if (files != null)
            for (File x : files) {
                if (x.isFile()) {
                    myFile.sonFile.put(x.getName(), new MyFile(x.getName(), x.length(), MyFile.TYPEFILE, "" + x.lastModified()));
                    myFile.sonFile.get(x.getName()).setPath(myFile.getPath() + "/" + x.getName());
                } else {
                    myFile.sonDirectory.put(x.getName(), new MyFile(x.getName(), x.length(), MyFile.TYPEFILEDERECTORY, "" + x.lastModified()));
                    myFile.sonDirectory.get(x.getName()).setPath(myFile.getPath() + "/" + x.getName());
                    fileToMyFile(x, myFile.sonDirectory.get(x.getName()));
                }
            }
    }

    public syncUploader(long id, String pos, File file, MainController mainController) {
        this.cloud = new SimpleStringProperty(pos);
        this.real = new SimpleStringProperty(file.getAbsolutePath());
        this.condition = new SimpleStringProperty("233");
        this.id = id;
        this.pos = pos;
        this.server = UserData.server;
        this.localFile = file;
        this.mainController = mainController;
        localMyFile = new MyFile(file.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + file.lastModified());
        localMyFile.setPath(file.getAbsolutePath());
    }

    private void buildPath(MyFile myFile) {
        for (MyFile x : myFile.sonFile.values()) {
            x.setPath(myFile.getPath() + "/" + x.getName());
            x.setId(myFile.getId());
        }
        for (MyFile x : myFile.sonDirectory.values()) {
            x.setPath(myFile.getPath() + "/" + x.getName());
            buildPath(x);
            x.setId(myFile.getId());
        }
    }
    private void buildPath(MyFile myFile,boolean flag) {
        for (MyFile x : myFile.sonFile.values()) {
            x.setPath(myFile.getPath() + "/" + x.getName());
            //x.setId(myFile.getId());
        }
        for (MyFile x : myFile.sonDirectory.values()) {
            x.setPath(myFile.getPath() + "/" + x.getName());
            buildPath(x);
            //x.setId(myFile.getId());
        }
    }

    private void deleteDirectory(HashMap<String, MyFile> data, MyFile myFile) {
        if (myFile.getType() == MyFile.TYPEFILE)
            return;
        for (MyFile x : myFile.sonDirectory.values()) {
            deleteDirectory(data, x);
        }
        if (!data.containsKey(myFile.getPath())) {
            try {
                server.deleteFileDirectory(myFile.getId(), pos, myFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFile(HashMap<String, MyFile> data, MyFile myFile) throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException {
        if (myFile.getType() == MyFile.TYPEFILEDERECTORY) {
            for (MyFile x : myFile.sonDirectory.values()) {
                deleteFile(data, x);
            }
        }else{
            if(!data.containsKey(myFile.getPath())){
                server.deleteFile(myFile.getId(),pos,myFile.getName());
            }
        }

    }

    private boolean createDirectory(HashMap<String, MyFile> data, MyFile myFile) throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, FileExistedException, ClassNotFoundException {
        for(MyFile x : myFile.sonDirectory.values()){
            if(!data.containsKey(x.getPath())){
                server.createDirectory(data.get(myFile.getPath()).getId(),pos,x.getName());
                return true;
            }else{
                boolean flag = createDirectory(data,x);
                if(flag)
                    return true;
            }
        }
        return false;
    }

    private void createFile(HashMap<String,MyFile> data,HashMap<String,MyFile> data2, MyFile myFile) throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, FileExistedException, ClassNotFoundException {
        for(MyFile x : myFile.sonDirectory.values()){
            createFile(data,data2,x);
        }
        for(MyFile x : myFile.sonFile.values()){
            if(!data.containsKey(x.getPath())){
                long id = data2.get(myFile.getPath()).getId();
                //server.createFile(id,pos,x.getName(),x.getSize(),Long.parseLong(x.getModifyTime()));
                YRL yrl = new YRL(id,pos,x.getName(),x.getPath(),YRL.TYPE_UPLOAD,MyDate.getCurTime(),x.getSize());
                SimpleLogListProperty simpleLogListProperty = new SimpleLogListProperty(yrl,mainController);
                simpleLogListProperty.start();
                transData.add(simpleLogListProperty);
            }
        }
    }

    private void deleteDirectory() throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException {
        cloudMyFile = server.getStructure(id, pos);
        cloudMyFile.setPath(localFile.getPath());
        transData.clear();
        localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
        localMyFile.setPath(localFile.getAbsolutePath());
        fileToMyFile(localFile, localMyFile);
        HashMap<String, MyFile> data = new HashMap<>();
        for (MyFile x : localMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILEDERECTORY)
                data.put(x.getPath(), x);
        }
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile);
        deleteDirectory(data, cloudMyFile);
    }

    private void deleteFile() throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException {
        cloudMyFile = server.getStructure(id, pos);
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile);
        transData.clear();
        localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
        localMyFile.setPath(localFile.getAbsolutePath());
        fileToMyFile(localFile, localMyFile);
        HashMap<String, MyFile> data = new HashMap<>();
        for (MyFile x : localMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILE)
                data.put(x.getPath(), x);
        }
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile);
        deleteFile(data, cloudMyFile);
    }

    private void createDirectory() throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException, FileExistedException {
        cloudMyFile = server.getStructure(id, pos);
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile);
        transData.clear();
        localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
        localMyFile.setPath(localFile.getAbsolutePath());
        fileToMyFile(localFile, localMyFile);
        HashMap<String,MyFile> data = new HashMap<>();
        for(MyFile x:cloudMyFile.toArray()){
            if(x.getType() == MyFile.TYPEFILEDERECTORY){
                data.put(x.getPath(),x);
            }
        }
        boolean flag = true;
        while(flag){
            flag = createDirectory(data,localMyFile);
            cloudMyFile = server.getStructure(id, pos);
            cloudMyFile.setPath(localFile.getPath());
            buildPath(cloudMyFile);
            data.clear();
            data = new HashMap<>();
            for(MyFile x:cloudMyFile.toArray()){
                if(x.getType() == MyFile.TYPEFILEDERECTORY){
                    data.put(x.getPath(),x);
                }
            }
        }
    }

    private void createFile() throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException, FileExistedException {
        cloudMyFile = server.getStructure(id, pos);
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile,true);
        transData.clear();
        localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
        localMyFile.setPath(localFile.getAbsolutePath());
        fileToMyFile(localFile, localMyFile);
        HashMap<String, MyFile> data = new HashMap<>();
        for(MyFile x : cloudMyFile.toArray()){
            if(x.getType() == MyFile.TYPEFILE){
                data.put(x.getPath(),x);
            }
        }
        HashMap<String,MyFile> data2 = new HashMap<>();
        for(MyFile x : cloudMyFile.toArray()){
            if(x.getType() == MyFile.TYPEFILEDERECTORY){
                data2.put(x.getPath(),x);
            }
        }
        createFile(data,data2,localMyFile);
    }

    public void work() throws NoAccessException, NoUserException, IOException, NoFileException, NoSuchUserException, ClassNotFoundException, FileExistedException {
        deleteDirectory();
        deleteFile();
        createDirectory();
        createFile();
    }

    public void sync() {
        try {
            work();
        } catch (ClassNotFoundException | NoUserException | NoFileException | IOException | NoAccessException | NoSuchUserException | FileExistedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            if (this.check()) {
                sync();
            }
            try {
                Thread.sleep(sync.TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
