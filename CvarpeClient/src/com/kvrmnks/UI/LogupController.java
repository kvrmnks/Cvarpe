package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.data.Password;
import com.kvrmnks.exception.*;
import com.kvrmnks.net.Client;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class LogupController implements Initializable {
    public Button logupButton;
    public Button backButton;
    public TextField userNameTextField;
    public PasswordField passwordTextField;
    public PasswordField passwordCheckTextField;
    public Label passwordCheckLabel;
    private Main application;

    public void setApp(Main app) {
        application = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void back(ActionEvent actionEvent) {
        application.setLoginForm();
    }

    private boolean isBothEmpty() {
        return passwordTextField.getText().equals("") && passwordCheckTextField.getText().equals("");
    }

    private void setEmpty() {
        passwordCheckLabel.setText("");
    }

    private void setRightLabel() {
        passwordCheckLabel.setText("√");
        passwordCheckLabel.setTextFill(Color.GREEN);
    }

    private void setWrongLabel() {
        passwordCheckLabel.setText("×");
        passwordCheckLabel.setTextFill(Color.RED);
    }

    private boolean checkEqual() {
        String password = passwordTextField.getText();
        String password2 = passwordCheckTextField.getText();
        return password.equals(password2);
    }

    public void passwordCheck(KeyEvent keyEvent) {
        if (isBothEmpty()) {
            setEmpty();
            return;
        }
        if (checkEqual())
            setRightLabel();
        else
            setWrongLabel();
    }

    public boolean checkPasswordStrength() {
        if (isBothEmpty()) {
            MyDialog.showErrorAlert("密码不能为空！");
            return false;
        }
        if (!checkEqual()) {
            MyDialog.showErrorAlert("两次输入不一致！");
            return false;
        }
        try {
            Password.isProper(passwordTextField.getText());
        } catch (PasswordTooFewException e) {
            MyDialog.showErrorAlert("密码长度至少为6！");
            return false;
        } catch (PasswordTooWeakException e) {
            MyDialog.showErrorAlert("密码应同时包含数字和字母！");
            return false;
        }
        return true;
    }

    private boolean logup() {
        try {
            boolean flag = Client.logUp(userNameTextField.getText(), passwordTextField.getText());
            if (!flag) {
                Log.log("注册失败");
                MyDialog.showErrorAlert("注册失败");

            } else {
                Log.log("注册成功");
                MyDialog.showInformationAlert("注册成功！");
                application.setLoginForm();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.log("注册失败");
            MyDialog.showErrorAlert("注册失败");
            return false;
        } catch (UserExistedException e) {
            Log.log("用户名已存在");
            MyDialog.showErrorAlert("用户名已存在");
            e.printStackTrace();
        }
        return true;
    }

    public void logup(ActionEvent actionEvent) {
        if (!checkPasswordStrength()) {
            return;
        }
        logup();
    }
}
