package com.kvrmnks.net;

import com.kvrmnks.data.Disk;
import com.kvrmnks.data.RealDisk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerReader extends UnicastRemoteObject implements NetReader {

    int pre = 0;

    public ServerReader() throws RemoteException {
    }

    private RandomAccessFile randomAccessFile;

    @Override
    public void setFile(long id, String name) throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(Disk.LOCATION + "_" + id + "_" + name, "r");
    }

    @Override
    public long length() throws IOException {
        return randomAccessFile.length();
    }

    @Override
    public void seek(long pos) throws IOException {
        randomAccessFile.seek(pos);
    }

    @Override
    public int getSize() throws RemoteException {
        return pre;
    }


    @Override
    public byte[] read(int begin, int length) throws IOException {
        byte[] buffer = new byte[length];
        pre = randomAccessFile.read(buffer,begin,length);
        return buffer;
    }

    @Override
    public void close() throws IOException {
        randomAccessFile.close();
    }


}
