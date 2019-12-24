package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.data.UserData;
import com.kvrmnks.exception.Log;
import com.kvrmnks.net.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Main application;
    @FXML
    public Button closeButton, loginButton;
    public TextField userNameTextField;
    public PasswordField passwordTextField;
  //  private Socket socket;
   // private String serverIp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        try {
            boolean flag = Client.logIn(userNameTextField.getText(),passwordTextField.getText());
            if (flag) {
                Log.log("链接成功");
                UserData.setUserName(userNameTextField.getText());
                UserData.setUserPassword(passwordTextField.getText());
                application.setMainForm();
            } else {
                Log.log("用户名或密码错误");
                MyDialog.showInformationAlert("用户名或密码错误");
            }
        } catch (IOException e) {
            Log.log("链接失败");
            MyDialog.showErrorAlert("连接失败");
            e.printStackTrace();
        }
    }

    public void setApp(Main app) {
        application = app;
    }

    public void logup(ActionEvent actionEvent) {
        application.setLogupForm();
    }

}
