package com.kvrmnks.data;

import java.io.Serializable;

public class InfoFile implements Serializable {
    private String md5;
    private long size;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
