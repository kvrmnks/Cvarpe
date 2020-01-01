package com.kvrmnks.net;

public class TransLoader implements Runnable{
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

    @Override
    public void run() {

    }
}
