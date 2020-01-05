package com.kvrmnks.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class YRLList implements Serializable{
    private YRLList(String name){
        this.name = name;
    }
    private String name;
    private Vector<YRL> yrlArrayList =  new Vector<>();

    public static YRLList getByUserName(String name){
        final YRLList ret ;//= new YRLList();
        YRLList ret1;
        File file = new File("__"+name+"__YRL.db");
        if(!file.exists()) ret1 = new YRLList(name);
        else{
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                ret1 = (YRLList) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                ret1 = null;
            }
        }
        if(ret1 == null) return null;
        ret = ret1;
        Runtime.getRuntime().addShutdownHook(new Thread(ret::save));
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

    public Vector<YRL> getYrlArrayList() {
        return yrlArrayList;
    }

    public void setYrlArrayList(Vector<YRL> yrlArrayList) {
        this.yrlArrayList = yrlArrayList;
    }

    public void remove(YRL x){
        this.yrlArrayList.remove(x);
    }

    public void add(YRL x){
        this.yrlArrayList.add(x);
      //  save();
    }

    public void clear(){
        yrlArrayList.clear();
    }
}
