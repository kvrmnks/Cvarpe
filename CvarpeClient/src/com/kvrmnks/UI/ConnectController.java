package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.Log;
import com.kvrmnks.net.Client;
import com.kvrmnks.net.Net;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {
    public Button connectButton, closeButton;
    public ComboBox ipTextField, portTextField;
    private Main application;

    public void setApp(Main app) {
        application = app;
    }

    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> ip = UserData.getPreferredIp();
        ArrayList<String> port = UserData.getPreferredPort();
        for (String x : ip)
            ipTextField.getItems().add(x);
        for (String x : port)
            portTextField.getItems().add(x);
        ipTextField.setValue((String)UserData.getPreviousIp());
        portTextField.setValue((String)UserData.getPreviousPort());
    }

    public void connect(ActionEvent actionEvent) {

        try {
            String ip = (String) ipTextField.getValue();
            int port = Integer.parseInt((String) (portTextField.getValue()));
            Net tmp = (Net) Naming.lookup("rmi://" + ip + ":" + port + "/" + "Server");
            UserData.setServerIp("rmi://" + ip + ":" + port + "/" + "Server");
            UserData.setReaderIp("rmi://" + ip + ":" + port + "/" + "ServerReader");
            UserData.setWriterIp("rmi://" + ip + ":" + port + "/" + "ServerWriter");
            Client.setServerIp(ip);
            Client.setPort(port);
            Client.setNet(tmp);
            Log.log("链接成功 ip:" + ip + " port:" + port);

            if(!UserData.getPreferredIp().contains(ip))
                UserData.getPreferredIp().add(ip);
            if(!UserData.getPreferredPort().contains(""+port))
                UserData.getPreferredPort().add(""+port);
            UserData.setPreviousIp(ip);
            UserData.setPreviousPort(""+port);

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

