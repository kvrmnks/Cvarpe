package com.kvrmnks.data;

import com.kvrmnks.net.DownLoader;
import com.kvrmnks.net.Uploader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

public class SimpleLogListProperty {

    private final SimpleStringProperty type, name, time;
    private final SimpleStringProperty size;
    private final SimpleDoubleProperty process;
    private final ProgressIndicator progressBar;
    private final HBox hbox;
    private final Label label;
    private Uploader uploader;
    private DownLoader downLoader;
    public static final int TYPE_DOWNLOAD = 1;
    public static final int TYPE_UPLOAD = 2;
    private TransData transData;
    private void build() {
        this.label.textProperty().unbind();
        this.label.textProperty().bind(this.process.multiply(100).asString().concat("%"));
        this.progressBar.progressProperty().unbind();
        this.progressBar.progressProperty().bind(this.process);
        this.hbox.getChildren().addAll(this.progressBar, this.label);
    }

    public SimpleLogListProperty(int type, String name, String time, long size, double process, TransData transData) {
        this.type = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.time = new SimpleStringProperty();
        this.size = new SimpleStringProperty();
        this.process = new SimpleDoubleProperty();
        this.transData = transData;
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
        this.hbox = new HBox();
        this.label = new Label();
        build();
    }

    public Uploader getUploader() {
        return uploader;
    }

    public TransData getTransData() {
        return transData;
    }

    public void setTransData(TransData transData) {
        this.transData = transData;
    }

    public void setDownLoader(DownLoader downLoader){}

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public HBox getHBox() {
        return hbox;
    }

    public Label getLabel() {
        return label;
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
