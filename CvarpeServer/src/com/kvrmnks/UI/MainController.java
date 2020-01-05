package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.ExceptionSolver;
import com.kvrmnks.exception.Log;
import com.kvrmnks.exception.NoUserException;
import com.kvrmnks.exception.UserExistedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static TextArea Q;
    public TextField nameTextField;
    public TextField passwordTextField;
    public Button addButton;
    public ContextMenu contextMenu;
    public MenuItem modifyPasswordMenuItem;
    public MenuItem deleteMenuItem;
    public Button logSaveButton;
    public TextArea logTextArea;
    public TableColumn<SimpleUserProperty, String> userUsedCapacityTableColumn;
    public TableColumn<SimpleUserProperty, String> userCapacityTableColumn;
    public TableColumn<SimpleUserProperty, String> userLastLoginTableColumn;
    Main application;
    @FXML
    public TableView<SimpleUserProperty> tableView;
    public TableColumn<SimpleUserProperty, String> nameTableColumn;
    public TableColumn<SimpleUserProperty, String> passwordTableColumn;
    private SimpleUserProperty simpleUserProperty;
    public static ObservableList<SimpleUserProperty> data = FXCollections.observableArrayList();


    private void setSimpleUserProperty(SimpleUserProperty simpleUserProperty) {
        this.simpleUserProperty = simpleUserProperty;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Q = logTextArea;
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        userCapacityTableColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        userUsedCapacityTableColumn.setCellValueFactory(new PropertyValueFactory<>("usedCapacity"));
        userLastLoginTableColumn.setCellValueFactory(new PropertyValueFactory<>("userLastLogin"));

        tableView.setRowFactory(param -> {
            TableRow<SimpleUserProperty> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                TableRow<SimpleUserProperty> r = (TableRow<SimpleUserProperty>) event.getSource();
                SimpleUserProperty sup = r.getItem();
                if (sup == null) return;

                setSimpleUserProperty(sup);
                if (event.getButton() == MouseButton.SECONDARY) {
                    Log.log(sup.toString());
                }
            });
            return row;
        });

        tableView.setItems(data);
        flushUserTable();
    }

    public void flushUserTable() {
        data.clear();
        User[] user = DataBase.getAllUser();
        for (User u : user) {
            data.add(new SimpleUserProperty(u));
        }
    }

    public void setApp(Main app) {
        application = app;
    }

    public void add(ActionEvent actionEvent) {
        try {
            UserManager.addUser(nameTextField.getText(), MD5.getMD5(passwordTextField.getText()));
            nameTextField.setText("");
            passwordTextField.setText("");
            flushUserTable();
        } catch (UserExistedException | IOException e) {
            MyDialog.showErrorAlert("同样的用户名已存在!");
        }
    }

    public void modifyPassword(ActionEvent actionEvent) {
        try {
            UserManager.modifyPassword(simpleUserProperty.getName()
                    , MyDialog.showTextInputDialog("新的密码"));
            flushUserTable();
        } catch (NoUserException e) {
            ExceptionSolver.solve(e);
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        try {
            if (MyDialog.showCheckAlert("是否确认删除"))
                UserManager.deleteUser(simpleUserProperty.getName());
            flushUserTable();
        } catch (NoUserException e) {
            ExceptionSolver.solve(e);
        }
    }

    public void logSave(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要保存的目录");
        File file = directoryChooser.showDialog(application.getStage());
        if(file == null) return;
        File infoFile = new File(file.getAbsoluteFile()+"/info.log");
        if(infoFile.exists())
            infoFile.delete();
        infoFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(infoFile));
        printWriter.println(this.logTextArea.getText());
        printWriter.flush();
        printWriter.close();
    }

    public void logClear(ActionEvent actionEvent) {
        this.logTextArea.clear();
    }
}
