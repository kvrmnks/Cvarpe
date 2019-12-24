package com.kvrmnks.data;

import java.io.Serializable;

public class TransData implements Serializable {
    private String time,pos, name, realPos;
    private long size;
    private int type;
    public static final int TYPE_UPLOAD = 2;
    public static final int TYPE_DOWNLOAD = 1;

    private TransData() {
    }

    public TransData(String pos, String name, String realPos, int type) {
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealPos() {
        return realPos;
    }

    public void setRealPos(String realPos) {
        this.realPos = realPos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
