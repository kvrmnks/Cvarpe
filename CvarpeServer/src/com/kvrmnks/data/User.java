package com.kvrmnks.data;

import java.io.Serializable;

public final class User implements Serializable {
    private String name, password;
    private long capacity,usedCapacity;
    private long lastLogin;


    public User(String name,String password,long lastLogin){
        this.name = name;
        this.password = password;
        this.capacity = 1024L*1024L*1024L*10L;
        this.usedCapacity = 0;
        this.lastLogin = lastLogin;

    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public void addUsedCapacity(long delta){
        this.usedCapacity += delta;
    }
}
