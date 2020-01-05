package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.NoAccessException;
import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoSuchUserException;
import com.kvrmnks.exception.NoUserException;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class syncDownloader extends sync implements Runnable{
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

    public syncDownloader(long id, String pos, File file, MainController mainController) {
        this.cloud = new SimpleStringProperty(pos);
        this.real = new SimpleStringProperty(file.getAbsolutePath());
        this.condition = new SimpleStringProperty("233");
        this.id = id;
        this.pos = pos;
        this.server = UserData.server;
        this.localFile = file;
        this.mainController = mainController;
        //cloudMyFile = server.getStructure(id, pos);

        localMyFile = new MyFile(file.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + file.lastModified());
        localMyFile.setPath(file.getAbsolutePath());

        //fileToMyFile(file, localMyFile);
    }

    public void work() {

        transData.clear();

        HashMap<String, MyFile> data = new HashMap<>();
        for (MyFile x : localMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILEDERECTORY)
                data.put(x.getPath(), x);
        }
        cloudMyFile.setPath(localFile.getPath());
        buildPath(cloudMyFile);
        for (MyFile x : cloudMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILE)
                continue;
            if (!data.containsKey(x.getPath())) {
                File file = new File(x.getPath());
                file.mkdir();
            }
        }

        data.clear();
        localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
        localMyFile.setPath(localFile.getAbsolutePath());
        fileToMyFile(localFile, localMyFile);
        for (MyFile x : localMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILE)
                data.put(x.getPath(), x);
        }
        for (MyFile x : cloudMyFile.toArray()) {
            if (x.getType() == MyFile.TYPEFILEDERECTORY)
                continue;
            if (!data.containsKey(x.getPath())) {
                YRL yrl = new YRL(x.getId(), pos, x.getName(), x.getPath(), YRL.TYPE_DOWNLOAD, MyDate.getCurTime(), x.getSize());
                SimpleLogListProperty simpleLogListProperty = new SimpleLogListProperty(yrl, mainController);
                simpleLogListProperty.start();
                transData.add(simpleLogListProperty);
            } else if ((x.getSize() != data.get(x.getPath()).getSize())) {
                File file = new File(x.getPath());
                file.delete();
                YRL yrl = new YRL(x.getId(), pos, x.getName(), x.getPath(), YRL.TYPE_DOWNLOAD, MyDate.getCurTime(), x.getSize());
                SimpleLogListProperty simpleLogListProperty = new SimpleLogListProperty(yrl, mainController);
                simpleLogListProperty.start();
                transData.add(simpleLogListProperty);
            }
        }
    }

    public void sync() {
        try {
            cloudMyFile = server.getStructure(id, pos);
            localMyFile = new MyFile(localFile.getName(), 0L, MyFile.TYPEFILEDERECTORY, "" + localFile.lastModified());
            localMyFile.setPath(localFile.getAbsolutePath());
            fileToMyFile(localFile, localMyFile);
            work();
        } catch (ClassNotFoundException | NoUserException | NoFileException | IOException | NoAccessException | NoSuchUserException e) {
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
