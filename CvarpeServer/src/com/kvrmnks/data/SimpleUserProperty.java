package com.kvrmnks.data;

import javafx.beans.property.SimpleStringProperty;

public final class SimpleUserProperty {
    private final SimpleStringProperty name;
    private final SimpleStringProperty password;
    private SimpleStringProperty capacity,usedCapacity;
    private SimpleStringProperty userLastLogin;
    private User user;

    public SimpleUserProperty(User a) {
        name = new SimpleStringProperty(a.getName());
        password = new SimpleStringProperty(a.getPassword());
        capacity = new SimpleStringProperty(MyFile.transferSize(a.getCapacity()));
        usedCapacity = new SimpleStringProperty(MyFile.transferSize(a.getUsedCapacity()));
        userLastLogin = new SimpleStringProperty(MyDate.convert(""+a.getLastLogin()));
        this.user = a;
    }

    public String getUserLastLogin() {
        return userLastLogin.get();
    }

    public SimpleStringProperty userLastLoginProperty() {
        return userLastLogin;
    }

    public void setUserLastLogin(String userLastLogin) {
        this.userLastLogin.set(userLastLogin);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCapacity() {
        return capacity.get();
    }

    public SimpleStringProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity.set(capacity);
    }

    public String getUsedCapacity() {
        return usedCapacity.get();
    }

    public SimpleStringProperty usedCapacityProperty() {
        return usedCapacity;
    }

    public void setUsedCapacity(String usedCapacity) {
        this.usedCapacity.set(usedCapacity);
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

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
