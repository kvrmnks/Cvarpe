package com.kvrmnks.exception;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.MyDate;
import javafx.application.Platform;

public class Log {
    public static MainController mainController;
    public static void log(final String s) {
        Platform.runLater(()->{
            String c = MyDate.getCurTime() + "\n" + s + "\n";
          System.out.print(c);
            mainController.logTextArea.appendText(c+"\n");
        });
    }
}