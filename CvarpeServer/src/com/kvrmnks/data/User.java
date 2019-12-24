package com.kvrmnks.data;

import java.io.Serializable;

public final class User implements Serializable {
    private String name, password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
