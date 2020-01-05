package com.kvrmnks.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NetReader extends Remote {
    void setFile(long id, String name) throws RemoteException, FileNotFoundException;

    long length(long id) throws IOException;

    void seek(long id,long pos) throws IOException;

    int getSize(long id) throws RemoteException;

    byte[] read(long id,int begin, int length) throws IOException;

    void close(long id) throws IOException;
}
