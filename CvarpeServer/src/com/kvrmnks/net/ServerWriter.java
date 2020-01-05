package com.kvrmnks.net;

import com.kvrmnks.data.DataBase;
import com.kvrmnks.data.Disk;
import com.kvrmnks.data.RealDisk;
import com.kvrmnks.exception.Log;
import com.kvrmnks.exception.NoFileException;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWriter extends UnicastRemoteObject implements NetWriter {

    public ConcurrentHashMap<Long, DataOutputStream> dataOutputStreamTreeMap = new ConcurrentHashMap<>();
    //  public TreeMap<Long,FileLock> fileLockTreeMap = new TreeMap<>();

    public ServerWriter() throws RemoteException {
    }

    @Override
    synchronized public void setFile(long id, String name) throws IOException {
        Log.log("setFile " + id + " " + name);

        File file = new File(Disk.LOCATION + "_" + id + "_" + name);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file,true));
        //dataOutputStream.seek(dataOutputStream.length());
        dataOutputStreamTreeMap.put(id,dataOutputStream);


    }

    @Override
    synchronized public void close(long id) throws IOException {
        Log.log("close " + id);
        // fileLockTreeMap.get(id).release();
        // dataOutputStreamTreeMap.get(id).getChannel().close();/
        dataOutputStreamTreeMap.get(id).close();
        dataOutputStreamTreeMap.remove(id);
    }

    @Override
    synchronized public void write(long id, byte[] buffer, int begin, int length) throws IOException {
        Log.log("write " + id);
       /*
        File file = null;
        try {
            file = new File(Disk.LOCATION + "_" + id + "_" + DataBase.getMyFileById(id).getName());
        } catch (NoFileException e) {
            e.printStackTrace();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsoluteFile(), "rw");
        FileLock flout = null;
        while (true) {
            try {
                flout = fcout.tryLock(0L, Long.MAX_VALUE, true);
                break;
            } catch (Exception e) {
            }
        }
        randomAccessFile.seek(randomAccessFile.length());
        randomAccessFile.write(buffer,begin,length);
        randomAccessFile.close();
        */
         dataOutputStreamTreeMap.get(id).write(buffer, begin, length);
    }
}