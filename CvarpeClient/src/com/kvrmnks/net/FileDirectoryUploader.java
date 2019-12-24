package com.kvrmnks.net;

import com.kvrmnks.data.*;

import java.io.*;

public class FileDirectoryUploader {

    private String fileLocation, panLocation, serverIp;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
   // private FileStructure fileStructure;
    private SimpleLogListProperty[] simpleLogListProperties;

    public FileDirectoryUploader(
            String fileLocation
            , String panLocation
            , String serverIp
            , ObjectInputStream socketIn
            , ObjectOutputStream socketOut
    ) {
        this.fileLocation = fileLocation;
        this.panLocation = panLocation;
        this.serverIp = serverIp;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
     //   fileStructure = new FileStructure();
    }
/*
    private void build() {
        IOFile ioFile = new IOFile();
        ioFile.input(fileLocation);
        fileStructure.setMyfile(ioFile.getList());
    }

    private void createFileDirectory() throws IOException {
        MyFile[] myFiles = fileStructure.getFileDirectory();
        for (MyFile myFile : myFiles) {
            socketOut.writeUTF("CreateDirectory$" + panLocation + "/" + myFile.getPath());
            socketOut.flush();
        }
    }

    private void initProperties() {
        MyFile[] myFiles = fileStructure.getFile();
        simpleLogListProperties = new SimpleLogListProperty[myFiles.length];
        for (int i = 0; i < myFiles.length; i++) {
            simpleLogListProperties[i] = new SimpleLogListProperty(
                    SimpleLogListProperty.TYPE_UPLOAD
                    , myFiles[i].getName()
                    , ""
                    , myFiles[i].getSize()
                    , 0
            );
        }
    }

    public SimpleLogListProperty[] getProperties() {
        return simpleLogListProperties;
    }

    public void init() throws IOException {
        build();
        createFileDirectory();
        initProperties();
    }

    public void upload() throws IOException {
        File file = new File(fileLocation);
        String floder = file.getParent();
        MyFile[] myFiles = fileStructure.getFile();
        for (int i = 0; i < myFiles.length; i++) {
            socketOut.writeUTF(
                    "UploadFile$" + panLocation + "/" + myFiles[i].getParent() + "$" + myFiles[i].getName());
            socketOut.flush();
            int port = socketIn.readInt();
            Uploader uploader = new Uploader(
                    floder + "/" + myFiles[i].getPath()
                    , port
                    , serverIp
                    , simpleLogListProperties[i]
            );
            Thread thread = new Thread(uploader);
            thread.start();
        }
    }


 */
}
