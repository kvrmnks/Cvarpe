package com.kvrmnks.data;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyDialog {
    public static void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        Platform.runLater(alert::showAndWait);
    }

    public static void showInformationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        Platform.runLater(alert::showAndWait);
        //alert.showAndWait();
    }

    public static String showTextInputDialog(String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(content);
        return dialog.showAndWait().get();
    }

    public static boolean showCheckAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        // Optional<ButtonType> ret = alert.showAndWait();
        return alert.showAndWait().get() == ButtonType.OK;

    }
}
