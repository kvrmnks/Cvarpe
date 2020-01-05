package com.kvrmnks.net;

import com.kvrmnks.data.Disk;
import com.kvrmnks.data.RealDisk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ServerReader extends UnicastRemoteObject implements NetReader {

    //int pre = 0;

    public ServerReader() throws RemoteException {
    }
    private ConcurrentHashMap<Long,RandomAccessFile> randomAccessFileTreeMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long,Integer> pre = new ConcurrentHashMap<>();
    //private RandomAccessFile randomAccessFile;

    @Override
    synchronized public void setFile(long id, String name) throws FileNotFoundException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(Disk.LOCATION + "_" + id + "_" + name, "r");
        randomAccessFileTreeMap.put(id,randomAccessFile);
        pre.put(id,0);
    }

    @Override
    synchronized public long length(long id) throws IOException {
        return randomAccessFileTreeMap.get(id).length();
    }

    @Override
    synchronized public void seek(long id,long pos) throws IOException {
        randomAccessFileTreeMap.get(id).seek(pos);
    }

    @Override
    synchronized public int getSize(long id) throws RemoteException {
        return pre.get(id);
    }


    @Override
    synchronized public byte[] read(long id,int begin, int length) throws IOException {
        byte[] buffer = new byte[length];
        pre.replace(id,randomAccessFileTreeMap.get(id).read(buffer,begin,length));
        return buffer;
    }

    @Override
    synchronized public void close(long id) throws IOException {
        randomAccessFileTreeMap.get(id).close();
        randomAccessFileTreeMap.remove(id);
        pre.remove(id);
    }


}
