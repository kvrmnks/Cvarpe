package com.kvrmnks.net;

import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.TransData;
import com.kvrmnks.exception.ExceptionSolver;
import com.kvrmnks.exception.NoAccessException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DownLoader implements Runnable {
    private final static int PACKAGE_SIZE = 1024;
    private File realFile, infoFile;
    private int port;
    private String ip;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedOutputStream fileOut;
    private SimpleLogListProperty logListProperty;
    private TransData transData;
    private String md5 = "";
    private long currentLength = 0;
    private boolean flag = false;

    public void stop() {
        flag = true;
    }

    public DownLoader(TransData transData, String ip, int port, SimpleLogListProperty simpleLogListProperty) {
        this.transData = transData;
        this.ip = ip;
        this.port = port;
        logListProperty = simpleLogListProperty;
    }

    private void setConnect() throws IOException {
        socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void createRealFile() throws IOException {
        realFile = new File(transData.getRealPos() + "/" + transData.getName());
        if (!realFile.exists())
            realFile.createNewFile();
        else {
            currentLength = realFile.length();
        }
        fileOut = new BufferedOutputStream(new FileOutputStream(realFile));
    }

    private void createInfoFile() throws IOException {
        infoFile = new File(transData.getRealPos() + "/" + transData.getName() + ".info");
        if (!infoFile.exists())
            infoFile.createNewFile();
        else {
            Scanner sc = new Scanner(infoFile);
            if (sc.hasNext()) {
                md5 = sc.next();
            }
            sc.close();
        }
    }

    private void preWork() throws IOException {
        createRealFile();
        createInfoFile();
    }

    private void mainWork() throws IOException, NoAccessException {

        long len = in.readLong();
        String realMd5 = in.readUTF();
        if ((!md5.equals("")) && (!realMd5.equals(md5))) {
            throw new NoAccessException();
        }
        out.writeBoolean(true);
        out.flush();
        out.writeLong(currentLength);
        out.flush();
        if (md5.equals("")) {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(infoFile)), true);
            pw.println(realMd5);
            pw.close();
            md5 = realMd5;
        }
        fileOut = new BufferedOutputStream(new FileOutputStream(realFile));
        byte[] buffer = new byte[PACKAGE_SIZE];

        while (currentLength < len) {
            int cur = in.read(buffer);
            //System.out.println(cur+" "+currentLength+" "+len);
            fileOut.write(buffer, 0, cur);
            currentLength += cur;
            fileOut.flush();
            if (flag)
                return;

            double cur_prac = ((double) currentLength) / len;
            if (cur_prac - 0.01 > logListProperty.getProcess())
                logListProperty.setProcess((cur_prac));
        }
        logListProperty.setProcess(1);
        fileOut.close();
        infoFile.delete();
    }

    @Override
    public void run() {
        try {
            setConnect();
            preWork();
            mainWork();
            in.close();
            out.close();
            fileOut.close();
            socket.close();
        } catch (IOException | NoAccessException e) {
            ExceptionSolver.solve(e);
        }
        System.gc();
    }
}
