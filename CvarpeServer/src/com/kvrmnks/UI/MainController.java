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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TextField nameTextField;
    public TextField passwordTextField;
    public Button addButton;
    public ContextMenu contextMenu;
    public MenuItem modifyPasswordMenuItem;
    public MenuItem deleteMenuItem;
    Main application;
    @FXML
    public TableView tableView;
    public TableColumn nameTableColumn;
    public TableColumn passwordTableColumn;
    private SimpleUserProperty simpleUserProperty;
    ObservableList<SimpleUserProperty> data = FXCollections.observableArrayList();


    private void setSimpleUserProperty(SimpleUserProperty simpleUserProperty) {
        this.simpleUserProperty = simpleUserProperty;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        tableView.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
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
            }
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
            UserManager.addUser(nameTextField.getText(), passwordTextField.getText());
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
}
