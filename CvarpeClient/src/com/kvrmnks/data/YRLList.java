package com.kvrmnks.data;

import java.io.*;
import java.util.ArrayList;

public class YRLList implements Serializable{
    private YRLList(){}
    private String name;
    private ArrayList<YRL> yrlArrayList =  new ArrayList<>();

    public static YRLList getByUserName(String name){
        YRLList ret = new YRLList();
        File file = new File("__"+name+"__YRL.db");
        if(!file.exists()) return ret;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            ret = (YRLList) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ret.save();
        }
        return ret;
    }

    public void save(){
        File file = new File("__"+name+"__YRL.db");
        if(file.exists())
            file.delete();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YRL[] toArray(){
        YRL[] ret = new YRL[yrlArrayList.size()];
        yrlArrayList.toArray(ret);
        return ret;
    }

    public ArrayList<YRL> getYrlArrayList() {
        return yrlArrayList;
    }

    public void setYrlArrayList(ArrayList<YRL> yrlArrayList) {
        this.yrlArrayList = yrlArrayList;
    }

    public void add(YRL x){
        this.yrlArrayList.add(x);
        save();
    }

}
