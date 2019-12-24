package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.exception.Log;
import com.kvrmnks.net.Client;
import com.kvrmnks.net.Net;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {
    public Button connectButton, closeButton;
    public TextField ipTextField, portTextField;
    private Main application;

    public void setApp(Main app) {
        application = app;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void connect(ActionEvent actionEvent) {

        try {
            String ip = ipTextField.getText();
            int port = Integer.parseInt(portTextField.getText());
            Net tmp = (Net) Naming.lookup("rmi://"+ip+":"+port+"/"+"Server");
            Client.setServerIp(ip);
            Client.setPort(port);
            Client.setNet(tmp);
            Log.log("链接成功 ip:" + ip + " port:" + port);
            application.setLoginForm();
        } catch (NumberFormatException e) {
            MyDialog.showErrorAlert("端口格式有误");
        } catch (IOException e) {
            e.printStackTrace();
            MyDialog.showErrorAlert("无法连接");
        } catch (NotBoundException e) {
            MyDialog.showInformationAlert("找不到服务器");
        }

    }

    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }
}

