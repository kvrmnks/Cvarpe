package com.kvrmnks.net;

import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;

import java.io.*;

public class FileDirectoryDownLoader {
    private String fileLocation, serverIp, panLocation, panFileDirectoryName;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    // private FileStructure fileStructure;
    private MyFile root;
    private MyFile[] myFiles;
    private SimpleLogListProperty[] simpleLogListProperties;

    public FileDirectoryDownLoader(
            String fileLocation
            , String panLocation
            , String panFileDirectoryName
            , ObjectInputStream socketIn
            , ObjectOutputStream socketOut
            , String serverIp) {
        this.fileLocation = fileLocation;
        this.panLocation = panLocation;
        this.panFileDirectoryName = panFileDirectoryName;
        this.socketOut = socketOut;
        this.socketIn = socketIn;
        // fileStructure = new FileStructure();
        this.serverIp = serverIp;
    }
/*
    private void getFileList() throws IOException, ClassNotFoundException {
        socketOut.writeUTF("GetStructure$" + panLocation + panFileDirectoryName);
        socketOut.flush();
        root = (MyFile) socketIn.readObject();
        //fileStructure.receive(socketIn);
    }

    private void buildFileDirectory() {
        MyFile[] myFiles = root.getFileDirectory();
        for (MyFile myFile : myFiles) {
            File file = new File(fileLocation + "/" + myFile.getPath());
            if (!file.exists())
                file.mkdirs();
        }
    }

    private void getFileProperty() {
        myFiles = root.getFile();
        simpleLogListProperties = new SimpleLogListProperty[myFiles.length];
        for (int i = 0; i < myFiles.length; i++) {
            simpleLogListProperties[i] = new SimpleLogListProperty(
                    SimpleLogListProperty.TYPE_DOWNLOAD
                    , myFiles[i].getName()
                    , ""
                    , myFiles[i].getSize()
                    , 0
            );
        }
    }

    public void init() throws IOException, ClassNotFoundException {
        getFileList();
        buildFileDirectory();
        getFileProperty();
    }


    public void download() throws IOException {
        for (int i = 0; i < myFiles.length; i++) {
            String filePath = myFiles[i].getPath();
            socketOut.writeUTF("DownloadFile$" + panLocation + filePath);
            socketOut.flush();
            int port = socketIn.readInt();
            DownLoader downLoader = new DownLoader(
                    new File(fileLocation + filePath)
                    , port
                    , serverIp
                    , simpleLogListProperties[i]);
            Thread thread = new Thread(downLoader);
            thread.start();
        }
    }

    public SimpleLogListProperty[] getProperty() {
        return simpleLogListProperties;
    }

 */
}
