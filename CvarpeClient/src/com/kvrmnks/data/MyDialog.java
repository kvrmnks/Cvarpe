package com.kvrmnks.data;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.ArrayList;
import java.util.Optional;

public class MyDialog {
    public static void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        alert.showAndWait();
    }

    public static void showInformationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        alert.showAndWait();
    }

    public static String showTextInputDialog(String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(content);
        Optional<String> ret = dialog.showAndWait();
        return ret.get();
    }

    public static boolean showCheckAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        Optional<ButtonType> ret = alert.showAndWait();
        return ret.get() == ButtonType.OK;
    }
    public static String showChioceDialog(ArrayList<String> options,String title,String head,String content){
        ChoiceDialog<String> frame = new ChoiceDialog<>("临时可读链接", options);
        frame.setTitle(title);
        frame.setHeaderText(head);
        frame.setContentText(content);
        Optional<String> result = frame.showAndWait();
        return result.orElse(null);
    }
    public static String showInputDialog(String title,String head,String content){
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle(title);
        dialog.setHeaderText(head);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
