package com.kvrmnks.data;

import java.io.Serializable;
import java.util.ArrayList;

public class TransDataList implements Serializable {

    private ArrayList<TransData> transDataArrayList = new ArrayList<>();
    private String userName;

    public TransDataList(String userName){
        this.userName = userName;
    }

    public void addTranData(TransData x){
        transDataArrayList.add(x);
    }

    public ArrayList<TransData> getTransDataArrayList() {
        return transDataArrayList;
    }

    public void setTransDataArrayList(ArrayList<TransData> transDataArrayList) {
        this.transDataArrayList = transDataArrayList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TransData[] toArray() {
        TransData[] ret = new TransData[transDataArrayList.size()];
        transDataArrayList.toArray(ret);
        return ret;
    }

}
