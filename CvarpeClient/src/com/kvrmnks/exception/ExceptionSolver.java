package com.kvrmnks.exception;

import com.kvrmnks.data.MyDialog;

public class ExceptionSolver {
    public static void solve(Exception e) {
        MyDialog.showErrorAlert(e.getMessage());
        System.exit(0);
    }
}
