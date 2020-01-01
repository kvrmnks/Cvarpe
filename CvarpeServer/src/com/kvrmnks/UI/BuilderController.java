package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.DataBase;
import com.kvrmnks.data.MyDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BuilderController implements Initializable {
    public TextField targetTextField;

    Main application;
    @FXML
    public Button closeButton, buildButton;
    @FXML
    public ComboBox portTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        targetTextField.setText(DataBase.getHomePath());
        ArrayList<String> ports = DataBase.getPorts();
        for (String x : ports)
            portTextField.getItems().add(x);

    }

    public void setApp(Main app) {
        application = app;
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void build(ActionEvent actionEvent) {
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt((String) portTextField.getValue()));
            ss.close();
            if (!DataBase.getPorts().contains((String) portTextField.getValue()))
                DataBase.getPorts().add((String) portTextField.getValue());
            application.setMainForm(ss);
        } catch (NumberFormatException e) {
            MyDialog.showErrorAlert("端口格式有误");
        } catch (IOException e) {
            MyDialog.showInformationAlert("无法建立服务器");
        } catch (Exception e) {
            ;
        }
    }

    public void change(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择目标文件夹");
        File file = directoryChooser.showDialog(application.getStage());
        if (file == null) return;
        else {
            targetTextField.setText(file.getAbsolutePath());
            DataBase.setHomePath(file.getAbsolutePath());
        }
    }
}
