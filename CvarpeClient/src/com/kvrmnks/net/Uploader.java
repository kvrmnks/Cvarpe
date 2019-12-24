package com.kvrmnks.net;

import com.kvrmnks.data.MD5;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.TransData;
import com.kvrmnks.exception.ExceptionSolver;

import java.io.*;
import java.net.Socket;

public class Uploader implements Runnable {
    private final static int BUFFERSIZE = 1024;
    private TransData transData;
    private String ip;
    private int port;
    private Socket socket;
    private ObjectInputStream socketIn;
    private RandomAccessFile fileIn;
    private ObjectOutputStream socketOut;
    private SimpleLogListProperty logListProperty;
    private boolean flag = false;

    private Uploader() {
    }

    public Uploader(TransData transData, String ip, int port, SimpleLogListProperty logListProperty) {
        this.transData = transData;
        this.ip = ip;
        this.port = port;
        this.logListProperty = logListProperty;
    }

    public Uploader(String user, String pos, String name, String realPos, String ip, int port, SimpleLogListProperty logListProperty) {
      //  transData = new TransData(user, pos, name, realPos, TransData.TYPE_UPLOAD);
        this.ip = ip;
        this.port = port;
        this.logListProperty = logListProperty;
    }

    public void stop() {
        flag = true;
    }

    private void setConnect() throws IOException {
        socket = new Socket(ip, port);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
        fileIn = new RandomAccessFile(transData.getRealPos(), "r");
    }

    private void upload() throws IOException {
        long len = fileIn.length();
        socketOut.writeLong(fileIn.length());
        socketOut.flush();
        socketOut.writeUTF(MD5.getMD5OfFile(transData.getRealPos()));
        socketOut.flush();
        boolean ret = socketIn.readBoolean();
        if (!ret)
            return;
        byte[] buffer = new byte[BUFFERSIZE];
        long current = socketIn.readLong();
        fileIn.seek(current);
        while (current < len) {

            if (flag) {
                return;
            }

            int tmp = fileIn.read(buffer);
            socketOut.write(buffer, 0, tmp);
            socketOut.flush();
            current += tmp;
            double cur_prac = ((double) current) / len;
            if (cur_prac - 0.01 > logListProperty.getProcess())
                logListProperty.setProcess((cur_prac));
        }
        logListProperty.setProcess(1);

    }


    @Override
    public void run() {
        try {
            setConnect();
            upload();
            socket.close();
            socketIn.close();
            socketOut.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionSolver.solve(e);
        }

    }
}
