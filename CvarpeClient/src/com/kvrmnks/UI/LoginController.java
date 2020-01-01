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
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public RadioButton radioButton;
    private Main application;
    @FXML
    public Button closeButton, loginButton;
    public ComboBox<String> userNameTextField;
    public PasswordField passwordTextField;
  //  private Socket socket;
   // private String serverIp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameTextField.setPromptText("user name");
        passwordTextField.setPromptText("password");
        userNameTextField.getItems().addAll(UserData.getUserDB().keySet());
        userNameTextField.setValue(UserData.getPreviousUserName());
        passwordTextField.setText(UserData.getUserDB().getOrDefault(UserData.getPreviousUserName(),""));
        if(UserData.getUserDB().containsKey(userNameTextField.getValue())){
            if(!UserData.getUserDB().get(userNameTextField.getValue()).equals(""))
                radioButton.setSelected(true);
        }
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        try {
            boolean flag = Client.logIn(userNameTextField.getValue(),passwordTextField.getText());
            if (flag) {
                Log.log("链接成功");
                UserData.setUserName(userNameTextField.getValue());
                UserData.setUserPassword(passwordTextField.getText());

                if(radioButton.isSelected())
                    UserData.getUserDB().put(userNameTextField.getValue(),passwordTextField.getText());
                else
                    UserData.getUserDB().put(userNameTextField.getValue(),"");
                UserData.setPreviousUserName(userNameTextField.getValue());

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

    public void change(ActionEvent actionEvent) {
        if(UserData.getUserDB().containsKey(userNameTextField.getValue())){
            if(!UserData.getUserDB().get(userNameTextField.getValue()).equals(""))
                radioButton.setSelected(true);
        }
        passwordTextField.setText(UserData.getUserDB().getOrDefault(userNameTextField.getValue(),""));
    }
}
