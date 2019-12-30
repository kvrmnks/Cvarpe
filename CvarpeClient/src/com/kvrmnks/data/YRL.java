package com.kvrmnks.data;

public class YRL {
    private long id;
    private String pos,name,realPos;
    private int type;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIRECTORY = 2;

    public YRL(long id,String pos,String name,String realPos,int type){
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.realPos = realPos;
        this.type = type;
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
