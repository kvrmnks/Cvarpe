package com.kvrmnks.data;

import java.io.Serializable;

public class YRL implements Serializable {
    private long id;
    private String pos,name,realPos;
    private int type;
    private String time;
    private long size;
    private boolean isFinished = false;
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_DOWNLOAD = 2;

    public YRL(long id,String pos,String name,String realPos,int type,String time,long size){
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.type = type;
        this.time = time;
        this.size = size;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
