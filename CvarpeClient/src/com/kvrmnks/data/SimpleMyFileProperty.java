package com.kvrmnks.data;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SimpleMyFileProperty {
    private final SimpleStringProperty name;
    private final SimpleStringProperty size;
    private final SimpleStringProperty type;
    private final SimpleStringProperty modifyTime;
    private final SimpleStringProperty path;
    private final SimpleStringProperty location = new SimpleStringProperty("");
    private long Rsize;
    private long id;

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public SimpleMyFileProperty(MyFile my) {
        //System.out.println(my);
        String name = my.getName();
        long size = my.getSize();
        int type = my.getType();
        String modifyTime = my.getModifyTime();
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(MyFile.transferSize(size));
        this.type = new SimpleStringProperty(type == MyFile.TYPEFILE ? "文件" : "文件夹");
        this.modifyTime = new SimpleStringProperty(modifyTime);
        this.path = new SimpleStringProperty(my.getPath());
        Rsize = my.getSize();
        id = my.getId();
    }

    public SimpleMyFileProperty(String name, long size, int type, String modifyTime) {
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(MyFile.transferSize(size));
        this.type = new SimpleStringProperty(type == MyFile.TYPEFILE ? "文件" : "文件夹");
        this.modifyTime = new SimpleStringProperty(modifyTime);
        this.path = new SimpleStringProperty();
        Rsize = size;
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public long getRsize() {
        return Rsize;
    }

    public void setRsize(long rsize) {
        Rsize = rsize;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getModifyTime() {
        return modifyTime.get();
    }

    public SimpleStringProperty modifyTimeProperty() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime.set(modifyTime);
    }

    public Long getId() {
        return id;
    }
}
