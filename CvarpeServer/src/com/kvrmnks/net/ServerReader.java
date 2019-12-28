package com.kvrmnks.net;

import com.kvrmnks.data.RealDisk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerReader extends UnicastRemoteObject implements NetReader {

    public ServerReader() throws RemoteException {
    }

    private RandomAccessFile randomAccessFile;

    @Override
    public void setFile(long id, String name) throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(RealDisk.LOCATION + "_" + id + "_" + name, "r");
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
    public int read(byte[] buffer, int begin, int length) throws IOException {
        return randomAccessFile.read(buffer, begin, length);
    }

    @Override
    public void close() throws IOException {
        randomAccessFile.close();
    }


}
