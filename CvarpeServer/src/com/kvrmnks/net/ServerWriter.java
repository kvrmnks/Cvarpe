package com.kvrmnks.net;

import com.kvrmnks.data.RealDisk;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerWriter extends UnicastRemoteObject implements NetWriter {

    public BufferedOutputStream bufferedOutputStream;

    public ServerWriter() throws RemoteException {
    }
    @Override
    public void setFile(long id, String name) throws RemoteException,FileNotFoundException {
        File file = new File(RealDisk.LOCATION + "_" + id + "_" + name);
        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
    }

    @Override
    public void close() throws RemoteException,IOException {
        bufferedOutputStream.close();
    }

    @Override
    public void write(byte[] buffer, int begin, int length) throws IOException {
        bufferedOutputStream.write(buffer, begin, length);
    }
}
