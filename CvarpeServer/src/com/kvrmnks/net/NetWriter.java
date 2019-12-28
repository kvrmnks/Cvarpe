package com.kvrmnks.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetWriter extends Remote {
    void setFile(long id, String name) throws RuntimeException, FileNotFoundException, RemoteException;

    void close() throws RemoteException, IOException;

    void write(byte[] buffer, int begin, int length) throws RuntimeException, IOException;
}
