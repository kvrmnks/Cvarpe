package com.kvrmnks.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetReader extends Remote {
    void setFile(long id, String name) throws RemoteException, FileNotFoundException;

    long length() throws RemoteException, IOException;

    void seek(long pos) throws RemoteException, IOException;

    int read(byte[] buffer, int begin, int length) throws RemoteException, IOException;

    void close() throws RemoteException, IOException;
}
