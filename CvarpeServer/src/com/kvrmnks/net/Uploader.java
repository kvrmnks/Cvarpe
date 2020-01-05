package com.kvrmnks.net;

import com.kvrmnks.data.Disk;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.RealDisk;
import com.kvrmnks.exception.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@Deprecated
public class Uploader implements Runnable {
    private final static int PACKAGE_SIZE = 1024;
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedOutputStream fileOut;
    private String pos, name;
    private Disk disk;
    private MyFile myFile;
    private File infoFile, realFile;
    private long currentLength;
    private String md5;

    private Uploader() {
    }

    public Uploader(Disk disk, String pos, String name, ObjectOutputStream out) throws IOException {
        this.disk = disk;
        this.pos = pos;
        this.name = name;
        serverSocket = new ServerSocket(0);
        out.writeInt(serverSocket.getLocalPort());
        out.flush();
    }

    private ServerSocket findEmptyServerSocket() throws IOException {
        ServerSocket ss = new ServerSocket(0);
        return ss;
    }

    private void setConnect() {
        try {
            socket = serverSocket.accept();
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTempFile() throws NoAccessException, NoFileException, IOException {
        myFile = disk.getStructure(pos);
        if (myFile.sonFile.containsKey(name)) {
            if (!myFile.sonFile.containsKey(name + ".info")) {
                throw new NoAccessException();
            }
        } else {
            try {
                disk.createFile(pos, name);
                disk.createFile(pos, name + ".info");
            } catch (FileExistedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createRealFile() throws IOException {
        realFile = new File(Disk.LOCATION + "_" + myFile.sonFile.get(name).getId() + "_" + name);
        if (!realFile.exists())
            realFile.createNewFile();
        infoFile = new File(Disk.LOCATION + "_" + myFile.sonFile.get(name + ".info").getId() + "_" + name + ".info");
        if (!infoFile.exists())
            infoFile.createNewFile();

        currentLength = realFile.length();
        Scanner sc = new Scanner(infoFile);
        if (sc.hasNext())
            md5 = sc.next();
        else
            md5 = "";
        sc.close();
    }

    private void preWork() throws IOException, NoAccessException, NoFileException {
        createTempFile();
        createRealFile();
    }

    private void mainWork() throws IOException, NoAccessException, NoFileException {
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
            fileOut.write(buffer, 0, cur);
            currentLength += cur;

        }
        fileOut.close();
        disk.deleteFile(pos, name + ".info");
        infoFile.delete();
    }

    @Override
    public void run() {
        setConnect();
        try {
            preWork();
            mainWork();
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoAccessException | NoFileException e) {
            try {
                out.writeBoolean(false);
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } finally {
            try {
                disk = Disk.constructByUserName(disk.getUserName());
                disk.getStructure(pos).sonFile.get(name).setSize(currentLength);
                disk.mainTain();
            } catch (IOException | NoFileException | NoAccessException | NoSuchUserException e) {
                e.printStackTrace();
            }
        }
    }
}
