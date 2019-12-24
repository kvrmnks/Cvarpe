package com.kvrmnks.net;

import com.kvrmnks.data.Disk;
import com.kvrmnks.data.MD5;
import com.kvrmnks.exception.NoAccessException;
import com.kvrmnks.exception.NoFileException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class Downloader implements Runnable {
    private final static int PACKAGE_SIZE = 1024;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket realSocket;
    private File realFile;
    private RandomAccessFile fileIn;
    private String pos, name;
    private ServerSocket serverSocket;
    private Disk disk;

    private Downloader() {
    }

    public  Downloader(Disk disk, String pos, String name, ObjectOutputStream out) throws IOException {
        this.disk = disk;
        this.pos = pos;
        this.name = name;
        serverSocket = new ServerSocket(0);
        out.writeInt(serverSocket.getLocalPort());
        out.flush();
    }

    private void setConnect() {
        try {
            realSocket = serverSocket.accept();
            in = new ObjectInputStream(realSocket.getInputStream());
            out = new ObjectOutputStream(realSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void preWork() throws NoAccessException, NoFileException, FileNotFoundException {
        realFile = new File(Disk.LOCATION + "_" + disk.getStructure(pos).sonFile.get(name).getId() + "_" + name);
        fileIn = new RandomAccessFile(realFile.getAbsoluteFile(), "r");
    }

    private void mainWork() throws IOException {
        out.writeLong(realFile.length());
        out.writeUTF(MD5.getMD5OfFile(realFile.getAbsolutePath()));
        out.flush();
        if (!in.readBoolean()) return;
        long currentLength = in.readLong();
        fileIn.seek(currentLength);
        byte[] buffer = new byte[PACKAGE_SIZE];
        long len = realFile.length();
        while (currentLength < len) {
            int cur = fileIn.read(buffer);
            currentLength += cur;
            out.write(buffer, 0, cur);
            out.flush();
        }
        fileIn.close();
    }

    @Override
    public void run() {
        setConnect();
        try {
            preWork();
            mainWork();
            serverSocket.close();
            realSocket.close();
            in.close();
            out.close();
        } catch (IOException | NoFileException | NoAccessException e) {
            try {
                serverSocket.close();
                in.close();
                out.close();
                realSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}