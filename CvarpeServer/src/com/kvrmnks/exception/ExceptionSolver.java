package com.kvrmnks.exception;

import com.kvrmnks.data.MyDialog;
import javafx.application.Platform;

import java.sql.SQLException;

public class ExceptionSolver {
    public static void solve(Exception e) {
        MyDialog.showErrorAlert(e.getMessage());
        System.exit(0);
    }
}
