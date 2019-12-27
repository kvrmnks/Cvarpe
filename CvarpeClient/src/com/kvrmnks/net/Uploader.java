package com.kvrmnks.net;


import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.FileExistedException;
import com.kvrmnks.exception.NoAccessException;
import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoUserException;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Uploader implements Runnable, Serializable {
    private static final int PACKAGE_SIZE = 1024;
    private long id, curSize, wholeSize;
    private String pos, name, realPos;
    private SimpleLogListProperty simpleLogListProperty;
    private Net server;

    public Uploader(long id, String pos, String name, String realPos, SimpleLogListProperty simpleLogListProperty) throws IOException, NotBoundException, NoAccessException, NoUserException, NoFileException, FileExistedException, ClassNotFoundException {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.simpleLogListProperty = simpleLogListProperty;
        server = (Net) Naming.lookup(UserData.getIp());
        MyFile myFile = server.getStructure(id, pos);
        if (!myFile.sonFile.containsKey(name)) {
            server.createFile(id, pos, name);
        }
        if (!myFile.sonFile.containsKey(name + ".info")) {
            server.createFile(id, pos, name + ".info");
        }

    }


    @Override
    public void run() {

    }
}
