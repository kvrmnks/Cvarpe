package com.kvrmnks;

import com.kvrmnks.UI.ConnectController;
import com.kvrmnks.UI.LoginController;
import com.kvrmnks.UI.LogupController;
import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.UserData;
import com.kvrmnks.net.Net;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;

public class Main extends Application {
    private Stage stage;
    private Scene curScene;
    private Net server;

    public Scene getCurScene() {
        return curScene;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Cvarpe");
        primaryStage.getIcons().add(new Image("/logo.png"));
        setConnectForm();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        GridPane page;
        try {
            page = (GridPane) loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContentForBorder(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        BorderPane page;
        try {
            page = (BorderPane) loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContentForVBox(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        VBox page;
        try {
            page = (VBox) loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public void setConnectForm() {
        try {
            ConnectController connect = (ConnectController) replaceSceneContent("ConnectFXML.fxml");
            connect.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoginForm() {
        try {
            LoginController login = (LoginController) replaceSceneContentForVBox("LoginFXML.fxml");
            login.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainForm() {
        try {
            MainController mainform = (MainController) replaceSceneContentForBorder("MainFXML.fxml");
            mainform.setApp(this);
            mainform.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLogupForm() {
        try {
            LogupController logup = (LogupController) replaceSceneContent("LogupFXML.fxml");
            logup.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(UserData::save));
        launch(args);
    }
}
