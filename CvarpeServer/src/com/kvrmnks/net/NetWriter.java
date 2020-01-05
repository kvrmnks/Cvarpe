package com.kvrmnks.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetWriter extends Remote {
    void setFile(long id, String name) throws RuntimeException, IOException;

    void close(long id) throws IOException;

    void write(long id,byte[] buffer, int begin, int length) throws RuntimeException, IOException;
}
