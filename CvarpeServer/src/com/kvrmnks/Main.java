package com.kvrmnks;

import com.kvrmnks.UI.BuilderController;
import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.DataBase;
import com.kvrmnks.net.Server;
import com.kvrmnks.net.ServerReader;
import com.kvrmnks.net.ServerWriter;
import com.sun.javafx.sg.prism.NGNode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Main extends Application {
    private Stage stage;
    private Scene curScene;

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        GridPane page;
        try {
            page = loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContentForTab(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        TabPane page;
        try {
            page = loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Cvarpe");
        setBuilderForm();
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));
    }

    void setBuilderForm() {
        try {
            BuilderController bc = (BuilderController) replaceSceneContent("BuilderFXML.fxml");
            bc.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainForm(ServerSocket ss) {
        try {
            MainController mc = (MainController) replaceSceneContentForTab("MainFXML.fxml");
            mc.setApp(this);
            LocateRegistry.createRegistry(ss.getLocalPort());
            Naming.rebind("rmi://localhost:"+ss.getLocalPort()+"/Server",new Server(mc));
            Naming.rebind("rmi://localhost:"+ss.getLocalPort()+"/ServerReader",new ServerReader());
            Naming.rebind("rmi://localhost:"+ss.getLocalPort()+"/ServerWriter",new ServerWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DataBase::writeDB));
        launch(args);
    }
}