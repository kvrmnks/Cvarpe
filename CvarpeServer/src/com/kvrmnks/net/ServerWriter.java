package com.kvrmnks.net;

import com.kvrmnks.data.Disk;
import com.kvrmnks.data.RealDisk;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerWriter extends UnicastRemoteObject implements NetWriter {

    public DataOutputStream dataOutputStream;

    public ServerWriter() throws RemoteException {
    }
    @Override
    public void setFile(long id, String name) throws RemoteException,FileNotFoundException {
        File file = new File(Disk.LOCATION + "_" + id + "_" + name);
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
    }

    @Override
    public void close() throws RemoteException,IOException {
        dataOutputStream.close();
    }

    @Override
    public void write(byte[] buffer, int begin, int length) throws IOException {
        dataOutputStream.write(buffer, begin, length);
    }
}
