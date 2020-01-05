package com.kvrmnks.net;

import javafx.beans.property.SimpleStringProperty;

public class sync implements Runnable{
    public static final long TIME = 10000;

    protected SimpleStringProperty cloud,real,condition;

    public String getCondition() {
        return condition.get();
    }

    public SimpleStringProperty conditionProperty() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition.set(condition);
    }

    public String getCloud() {
        return cloud.get();
    }

    public SimpleStringProperty cloudProperty() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud.set(cloud);
    }

    public String getReal() {
        return real.get();
    }

    public SimpleStringProperty realProperty() {
        return real;
    }

    public void setReal(String real) {
        this.real.set(real);
    }

    @Override
    public void run() {

    }
}
