package com.kvrmnks.data;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.exception.*;
import com.kvrmnks.net.DownLoader;
import com.kvrmnks.net.TransLoader;
import com.kvrmnks.net.Uploader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.rmi.NotBoundException;

public class SimpleLogListProperty {

    private final SimpleStringProperty type, name, time,speed,condition;
    private final SimpleStringProperty size;
    private final SimpleDoubleProperty process;
    private final ProgressIndicator progressBar;
    private TransLoader transLoader;
    public static final int TYPE_DOWNLOAD = 1;
    public static final int TYPE_UPLOAD = 2;
    private YRL yrl;

    public boolean isFinished(){
        return yrl.isFinished();
    }

    public void setFinished(boolean flag){
        yrl.setFinished(flag);
    }

    public boolean currentCondition(){
        return transLoader.currentCondition();
    }
    public void reset(){
        transLoader.reset();
    }

    public void stop(){
        transLoader.stop();
        condition.setValue("暂停");
    }

    public void start(){
        transLoader.reset();
        condition.setValue("进行中");
        new Thread(transLoader).start();
    }

    private void build() {
        this.progressBar.progressProperty().unbind();
        this.progressBar.progressProperty().bind(this.process);
    }

    public SimpleLogListProperty(YRL yrl, MainController mainController){
        this.yrl = yrl;
        this.type = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.time = new SimpleStringProperty();
        this.size = new SimpleStringProperty();
        this.speed = new SimpleStringProperty();
        this.process = new SimpleDoubleProperty();
        this.progressBar = new ProgressIndicator(0);
        this.condition = new SimpleStringProperty();
        if(yrl.getType() == YRL.TYPE_DOWNLOAD)
            this.type.setValue("下载");
        else
            this.type.setValue("上传");
        this.name.setValue(yrl.getName());
        this.time.setValue(yrl.getTime());
        this.size.setValue(MyFile.transferSize(yrl.getSize()));
        if(yrl.getType() == YRL.TYPE_DOWNLOAD){
            transLoader = new DownLoader(yrl.getId(),yrl.getPos(),yrl.getName(),yrl.getRealPos(),this);
        }else{
            transLoader = new Uploader(yrl.getId(),yrl.getPos(),yrl.getName(),yrl.getRealPos(),this);
        }
        transLoader.setMainController(mainController);
        transLoader.stop();
        build();
        if(yrl.isFinished()){condition.setValue("完成");this.setProcess(1);}
        else{;}
    }
    @Deprecated
    public SimpleLogListProperty(int type, String name, String time, long size, double process,TransLoader transLoader) {
        this.type = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.time = new SimpleStringProperty();
        this.size = new SimpleStringProperty();
        this.speed = new SimpleStringProperty();
        this.process = new SimpleDoubleProperty();
        this.condition = new SimpleStringProperty("进行中");
        if (type == TYPE_DOWNLOAD) {
            this.type.setValue("下载");
        } else {
            this.type.setValue("上传");
        }
        this.name.setValue(name);
        this.size.setValue(MyFile.transferSize(size));
        this.process.setValue(process);
        this.time.setValue(time);
        this.progressBar = new ProgressIndicator(process);
        this.transLoader = transLoader;
        build();
    }

    public void init() throws IOException, NotBoundException, NoUserException, FileExistedException, FileStructureException, NoFileException, NoAccessException, NoSuchUserException, ClassNotFoundException {
        transLoader.init();
    }

    public String getCondition() {
        return condition.get();
    }

    public SimpleStringProperty conditionProperty() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition.set(condition);
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public YRL getYrl() {
        return yrl;
    }

    public void setYrl(YRL yrl) {
        this.yrl = yrl;
    }

    public TransLoader getTransLoader() {
        return transLoader;
    }

    public void setTransLoader(TransLoader transLoader) {
        this.transLoader = transLoader;
    }

    public ProgressIndicator getProgressBar() {
        return progressBar;
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTime() {
        return time.get();
    }

    public String getSpeed() {
        return speed.get();
    }

    public SimpleStringProperty speedProperty() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed.set(speed);
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public double getProcess() {
        return process.get();
    }

    public SimpleDoubleProperty processProperty() {
        return process;
    }

    public void setProcess(double process) {
        this.process.set(process);
    }
}
