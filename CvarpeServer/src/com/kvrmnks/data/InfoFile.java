package com.kvrmnks.data;

import java.io.Serializable;

public class InfoFile implements Serializable {
    private String modifyTime;
    private long size;

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String md5) {
        this.modifyTime = md5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
