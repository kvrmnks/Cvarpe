package com.kvrmnks.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MyFile implements Serializable {


    private String name, modifyTime, path;
    private long size;
    public HashMap<String, MyFile> sonFile = new HashMap<>();
    public HashMap<String, MyFile> sonDirectory = new HashMap<>();

    private int type;
    private long id;
    public static final int TYPEFILE = 0;
    public static final int TYPEFILEDERECTORY = 1;

    public static String getUserNameFormURL(String url) {
        return url.split("/")[1];
    }

    public String getParent() {
        String str = path;
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) if (str.charAt(i) == '/') cnt++;
        if (cnt == 1)
            return str;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') cnt--;
            if (cnt == 0) break;
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static String backFroward(String str) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) if (str.charAt(i) == '/') cnt++;
        if (cnt == 2)
            return str;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') cnt--;
            sb.append(str.charAt(i));
            if (cnt == 1) break;
        }
        return sb.toString();
    }

    public MyFile() {
    }

    public MyFile(String name, Long size, int type, String modifyTime) {
        this.name = name;
        this.modifyTime = modifyTime;
        this.size = size;
        this.type = type;
    }

    public MyFile(String name, String modifyTime, Long size, int type, long id) {
        this.name = name;
        this.modifyTime = modifyTime;
        this.size = size;
        this.type = type;
        this.id = id;
        // this.fatherId = fatherId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyFile[] getCurrentFileList() {
        ArrayList<MyFile> list = new ArrayList<>();
        for (MyFile x : sonFile.values())
            list.add(x);
        for (MyFile x : sonDirectory.values())
            list.add(x);
        MyFile[] ret = new MyFile[list.size()];
        list.toArray(ret);
        return ret;
    }

    public void toArray(ArrayList<MyFile> list, MyFile x) {
        list.add(x);
        for (MyFile myFile : x.sonDirectory.values()) {
            toArray(list, myFile);
        }
        for (MyFile myFile : x.sonFile.values()) {
            toArray(list, myFile);
        }
    }

    public MyFile[] toArray() {
        ArrayList<MyFile> ret = new ArrayList<>();
        toArray(ret, this);
        MyFile[] myFiles = new MyFile[ret.size()];
        ret.toArray(myFiles);
        return myFiles;
    }

    public MyFile[] search(String cmd) {
        MyFile[] whole = toArray();
        ArrayList<MyFile> list = new ArrayList<>();
        for (MyFile mf : whole) {
            if (mf.getName().contains(cmd)) {
                list.add(mf);
            }
        }
        MyFile[] ret = new MyFile[list.size()];
        list.toArray(ret);
        return ret;
    }

    public static String transferSize(long size) {
        String str;
        double num;
        DecimalFormat df = new DecimalFormat("#.##");
        if (size < 1024)
            str = df.format(size) + "B";
        else if (size < 1024 * 1024) {
            num = (double) size / 1024;
            str = df.format(num) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            num = (double) size / (1024 * 1024);
            str = df.format(num) + "MB";
        } else {
            num = (double) size / (1024 * 1024 * 1024);
            str = df.format(num) + "GB";
        }
        return str;
    }

    @Override
    public String toString() {
        return ("name:" + name + " modifyTime:" + modifyTime + " path:" + path + " size:" + size + " type:" + type
                + " id:" + id);
    }
}
