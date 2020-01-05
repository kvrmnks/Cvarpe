package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.*;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class TransLoader implements Runnable{

    protected MainController mainController;

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public MainController getMainController(){
        return this.mainController;
    }

    protected boolean flag = false;

    public boolean currentCondition(){
        return flag;
    }

    public void stop(){
        flag = true;
    }

    public void reset(){
        flag = false;
    }
    public void init() throws IOException, NotBoundException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NoSuchUserException, FileExistedException, FileStructureException {}
    @Override
    public void run() {

    }
}
