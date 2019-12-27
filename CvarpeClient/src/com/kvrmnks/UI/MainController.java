package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.*;
import com.kvrmnks.exception.*;
import com.kvrmnks.net.Client;
import com.kvrmnks.net.FileDirectoryUploader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class MainController implements Initializable {
    public TableView<SimpleMyFileProperty> fileTableView;
    public TableColumn<SimpleMyFileProperty, String> fileTypeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> fileNameTableColumn;
    public TableColumn<SimpleMyFileProperty, Long> fileSizeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> fileModifyTimeTableColumn;
    public ContextMenu contextMenu;
    public MenuItem openMenuItem;
    public MenuItem downLoadMenuItem;
    public MenuItem renameMenuItem;
    public MenuItem removeMenuItem;
    public MenuItem bindMenuItem;
    public TextField pathTextField;
    public Button backForwardButton;
    public Button uploadButton;
    public Button flushButton;
    public MenuItem newFileDirectoryMenuList;
    public Button fileSearchButton;
    public TextField fileSearchTextField;
    public TableColumn<SimpleMyFileProperty, String> searchFileNameTableColumn;
    public TableColumn<SimpleMyFileProperty, String> searchFileLocationTableColumn;
    public TableColumn<SimpleMyFileProperty, Long> searchFileSizeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> searchFileModifyTimeTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyTypeTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyFileNameTableColumn;
    public TableColumn<SimpleLogListProperty, Long> modifyFileSizeTableColumn;
    public TableColumn<SimpleLogListProperty, ProgressIndicator> modifyProcessTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyTimeTableColumn;
    public TableView<SimpleMyFileProperty> searchTableView;
    public TableView<SimpleLogListProperty> logTableView;
    public Button uploadFileDirectoryButton;
    public MenuItem getSharedListMenuList;
    public ComboBox<String> shareListChooser;
    public Button shareListJumpButton;
    public Button shareListAddButton;
    public Button shareListBackButton;
    public TableColumn shareListTypeColumn;
    public TableColumn shareListNameColumn;
    public TableColumn shareListSizeColumn;
    public TableColumn shareListTimeColumn;
    public TableView shareListTableView;
    public MenuItem shareOpen;
    public MenuItem shareDownload;
    public MenuItem shareRename;
    public MenuItem shareRemove;
    public MenuItem shareBind;
    public MenuItem shareNewDirectory;
    private Main application;
    private ObservableList<SimpleMyFileProperty> data = FXCollections.observableArrayList();
    private ObservableList<SimpleMyFileProperty> searchResult = FXCollections.observableArrayList();
    private ObservableList<SimpleLogListProperty> logdata = FXCollections.observableArrayList();
    private ObservableList<SimpleMyFileProperty> shareList = FXCollections.observableArrayList();
    private SimpleMyFileProperty simpleMyFileProperty,shareMyFileProperty;
    private SimpleStringProperty currentPath;
    private Stack<Long> idStack = new Stack<>();
    private Stack<Long> shareIdStack = new Stack<>();
    private String curShareURL = "";

    private void initFileTab() {
        fileTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        fileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        fileModifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        fileTableView.setItems(data);
        fileTableView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    event.consume();
                }
            }
        });
        fileTableView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append("是否要将以下文件或文件夹上传？\n");
                    for (File file : db.getFiles()) {
                        sb.append(file.getAbsolutePath() + "\n");
                    }
                    boolean ret = MyDialog.showCheckAlert(sb.toString());
                    if (ret) {
                        for (File file : db.getFiles()) {
                            if (file.isFile()) {
                                try {
                                    upload(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    uploadFileDirectory(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    for (File file : db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        //System.out.println(filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        fileTableView.setRowFactory(new Callback<TableView<SimpleMyFileProperty>, TableRow<SimpleMyFileProperty>>() {
            @Override
            public TableRow<SimpleMyFileProperty> call(TableView<SimpleMyFileProperty> param) {
                TableRow<SimpleMyFileProperty> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                            try {
                                open(null);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(fileTableView, event.getScreenX(), event.getScreenY());
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                        }
                    }
                });

                return row;
            }
        });
    }

    private void initSearchTab() {
        searchFileLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        searchFileModifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        searchFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchFileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        searchTableView.setItems(searchResult);
    }

    private void initLogTab() {
        modifyFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        modifyFileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        modifyProcessTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator>, ObservableValue<ProgressIndicator>>() {
            @Override
            public ObservableValue<ProgressIndicator> call(TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator> param) {
                return new SimpleObjectProperty<ProgressIndicator>(param.getValue().getProgressBar());
            }
        });
        modifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        logTableView.setItems(logdata);
        // try {
          /*  TransDataList transDataList = DataBase.getTransDataListByName(UserData.getUserName());
            TransData[] transData = transDataList.toArray();
            for(TransData x : transData){
                //download();
                SimpleLogListProperty simpleLogListProperty = new SimpleLogListProperty(
                    x.getType()
                        ,x.getName()
                        ,x.getTime()
                        ,x.getSize()
                        ,0
                        ,x
                );
                logdata.add(simpleLogListProperty);
            }

           */
        //  } catch (NoSuchUserException ignored) {}

    }

    private void initShareTab(){
        shareListTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        shareListNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shareListSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        shareListTimeColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        shareListTableView.setItems(shareList);
        shareListTableView.setRowFactory(new Callback<TableView<SimpleMyFileProperty>, TableRow<SimpleMyFileProperty>>() {
            @Override
            public TableRow<SimpleMyFileProperty> call(TableView<SimpleMyFileProperty> param) {
                TableRow<SimpleMyFileProperty> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            shareMyFileProperty = r.getItem();
                            try {
                                //open(null);
                                shareOpen(null);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            //contextMenu.show(fileTableView, event.getScreenX(), event.getScreenY());
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            //setSimpleMyFileProperty(r.getItem());
                            shareMyFileProperty = r.getItem();
                        }
                    }
                });

                return row;
            }
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPath = new SimpleStringProperty("Editor^0^/" + UserData.getUserName() + "/");
        initFileTab();
        initSearchTab();
        initLogTab();
        initShareTab();;
    }

    void setSimpleMyFileProperty(SimpleMyFileProperty viewMyFile) {
        this.simpleMyFileProperty = viewMyFile;
    }

    public void setApp(Main app) {
        application = app;
    }

    public void init() {
        try {
            idStack.push(Client.getStructure(currentPath.getValueSafe()).getId());
            pathTextField.textProperty().bindBidirectional(currentPath);
            flush();
        } catch (ClassNotFoundException | NoUserException | NoAccessException | NoFileException | IOException e) {
            e.printStackTrace();
        }


    }


    public void flush() throws IOException, ClassNotFoundException {
        data.clear();
        MyFile file = null;
        try {
            file = Client.getStructure(idStack.peek(), currentPath.getValueSafe());
        } catch (NoUserException | NoAccessException | NoFileException e) {
            e.printStackTrace();
        }
       // data.add(new SimpleMyFileProperty(file));
        MyFile[] files = file.getCurrentFileList();
        for (MyFile mf : files) {
            data.add(new SimpleMyFileProperty(mf));
        }
    }

    public void shareFlush() throws IOException, ClassNotFoundException {
        shareList.clear();
        MyFile file = null;
        try {
            file = Client.getStructure(shareIdStack.peek(),curShareURL);
        } catch (NoUserException | NoAccessException | NoFileException e) {
            e.printStackTrace();
        }
        // data.add(new SimpleMyFileProperty(file));
        MyFile[] files = file.getCurrentFileList();
        for (MyFile mf : files) {
            shareList.add(new SimpleMyFileProperty(mf));
        }
    }
    public void open(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (simpleMyFileProperty == null) return;
        if(simpleMyFileProperty.getId() == idStack.peek()) return;
        currentPath.setValue(currentPath.getValueSafe() + simpleMyFileProperty.getName() + "/");
        idStack.push(simpleMyFileProperty.getId());
        flush();
    }

    public void shareOpen(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(shareMyFileProperty == null) return;
        if(shareMyFileProperty.getId() == shareIdStack.peek())return;
        shareIdStack.push(shareMyFileProperty.getId());
        shareFlush();
    }

    public void download(ActionEvent actionEvent) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NotBoundException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载目录");
        File f = directoryChooser.showDialog(application.getStage());
        if (f == null) return;
        if (simpleMyFileProperty == null) return;
        if(simpleMyFileProperty.getType().equals("文件夹"))return;
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(
                SimpleLogListProperty.TYPE_DOWNLOAD
                ,simpleMyFileProperty.getName()
                ,MyDate.getCurTime()
                ,simpleMyFileProperty.getRsize()
                ,0
                ,null
        );
        logdata.add(logListProperty);
        Client.downLoad(idStack.peek(),currentPath.getValueSafe(),simpleMyFileProperty.getName(),f.getAbsolutePath(),logListProperty);
        /*
        if (simpleMyFileProperty.getType().equals("文件夹")) {
            //System.out.println("233");
            SimpleLogListProperty[] sp = Client.downloadFileDirectory(f.getPath()
                    , currentPath.getValueSafe(), simpleMyFileProperty.getName());
            Collections.addAll(logdata, sp);
        } else if (simpleMyFileProperty.getType().equals("文件")) {
            SimpleLogListProperty logListProperty = new SimpleLogListProperty(
                    SimpleLogListProperty.TYPE_DOWNLOAD
                    , simpleMyFileProperty.getName()
                    , MyDate.getCurTime()
                    , simpleMyFileProperty.getRsize()
                    , 0
                    , null
            );
            logdata.add(logListProperty);

            Client.downLoad(
                    UserData.getUserName()
                    , currentPath.getValueSafe()
                    , simpleMyFileProperty.getName()
                    , f.getAbsoluteFile()
                    , Client.getServerIp()
                    , logListProperty
            );
        }

         */
    }

    private void reNameFile(long id,String pos, String name, String newName) throws IOException {
        try {
            Client.reNameFile(id,pos, name, newName);
            Log.log(MyDate.getCurTime() + "修改文件名成功");
            Log.log(pos + name + " -> " + pos + newName);
        } catch (ClassNotFoundException | NoAccessException | NoUserException | NoFileException | FileExistedException e) {
            Log.log("修改文件名失败");
            e.printStackTrace();
        }

    }

    private void reNameFileDirectory(long id,String pos, String name, String newName) throws IOException {
        try {
            Client.reNameFileDirectory(id,pos, name, newName);
            Log.log(MyDate.getCurTime() + "修改文件夹名成功");
            Log.log(pos + name + " -> " + pos + newName);
        } catch (NoUserException | ClassNotFoundException | NoAccessException | NoFileException | FileExistedException e) {
            Log.log(MyDate.getCurTime() + "修改文件夹名失败");
            Log.log(pos + name + " -> " + pos + name);
        }

    }

    public void reName(ActionEvent actionEvent) {
        if (simpleMyFileProperty == null) return;
        try {
            String newName = MyDialog.showTextInputDialog("输入新的文件名");
            if (newName == null || newName.equals("")) return;
            if (simpleMyFileProperty.getType().equals("文件夹")) {
                reNameFileDirectory(idStack.peek(),currentPath.getValueSafe(), simpleMyFileProperty.getName(), newName);
            } else {
                reNameFile(idStack.peek(),currentPath.getValueSafe(), simpleMyFileProperty.getName(), newName);
            }
            flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteFile(long id,String pos, String name) {
        try {
            Client.deleteFile(id,pos, name);
            return true;
        } catch (ClassNotFoundException | NoUserException | NoAccessException | NoFileException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteFileDirectory(long id,String pos, String name) throws IOException {
        try {
            Client.deleteFileDirectory(id,pos, name);
            return true;
        } catch (ClassNotFoundException | NoUserException | NoAccessException | NoFileException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if (MyDialog.showCheckAlert("是否确定删除")) {
                boolean flag = false;

                if (simpleMyFileProperty.getType().equals("文件夹"))
                    flag = deleteFileDirectory(idStack.peek(),currentPath.getValueSafe(), simpleMyFileProperty.getName());
                else
                    flag = deleteFile(idStack.peek(),currentPath.getValueSafe(), simpleMyFileProperty.getName());
                flush();
                //if (flag) {
                Log.log("删除" + currentPath.getValueSafe() + simpleMyFileProperty.getName() + (flag ? "成功" : "失败"));
                //}
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void binding(ActionEvent actionEvent) {
    }

    public void backForward(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(idStack.size() == 1) {
            flush();
            return;
        }
        currentPath.setValue(MyFile.backFroward(currentPath.getValueSafe()));
        idStack.pop();
        flush();
    }

    private void upload(File f) throws IOException {
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(
                SimpleLogListProperty.TYPE_UPLOAD
                , f.getName()
                , MyDate.getCurTime()
                , f.length()
                , 0
                , null
        );
        logdata.add(logListProperty);
        Client.upload(UserData.getUserName(), currentPath.getValueSafe(), f, Client.getServerIp(), logListProperty);
    }

    public void upload(ActionEvent actionEvent) throws IOException, ClassNotFoundException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要上传的文件");
        File f = fileChooser.showOpenDialog(application.getStage());
        if (f == null)
            return;
        upload(f);
        Thread.sleep(500);
        flush();
    }

    public void newFileDirectory(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String text = MyDialog.showTextInputDialog("输入文件夹名称");
        boolean flag = false;
        try {
            Client.createFileDirectory(idStack.peek(),currentPath.getValueSafe(), text);
            flag = true;
        } catch (NoUserException | NoAccessException | NoFileException | FileExistedException e) {
            e.printStackTrace();
        }
        Log.log("创建" + currentPath.getValueSafe() + "/" + text + (flag ? "成功" : "失败"));
        flush();
    }

    public void searchFile(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        searchResult.clear();
        MyFile[] myfile = new MyFile[0];
        try {
            myfile = Client.searchFile(fileSearchTextField.getText());
            for (MyFile x : myfile) {
                searchResult.add(new SimpleMyFileProperty(x));
            }
        } catch (NoFileException | NoAccessException | NoUserException e) {
            e.printStackTrace();
        }

    }

    private void uploadFileDirectory(File file) throws IOException {
        SimpleLogListProperty[] simpleLogListProperties = Client.uploadFileDirectory(file.getPath(), currentPath.getValueSafe());
        Collections.addAll(logdata, simpleLogListProperties);
    }

    public void uploadFileDirectory(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要上传的文件夹");
        File file = directoryChooser.showDialog(application.getStage());
        if (file == null)
            return;
        uploadFileDirectory(file);
        flush();
    }

    public void getSharedList(ActionEvent actionEvent) throws RemoteException {
        if(simpleMyFileProperty == null) return;
        ArrayList<String> options = new ArrayList<>();
        options.add("只读永久链接");
        options.add("可读可写永久链接");
        options.add("临时可读链接");
        options.add("临时可读可写链接");
        String result = MyDialog.showChioceDialog(options, "选择需要的链接类型", "选择需要的链接类型", "选择需要的链接类型");
        if (result == null) return;
        else {
            String cmd = "";
            if (result.equals(options.get(0))) {
                cmd = Client.getReadOnlyURL(simpleMyFileProperty.getId(),currentPath.getValueSafe());
            } else if (result.equals(options.get(1))) {
                cmd = Client.getReadAndWriteURL(simpleMyFileProperty.getId(),currentPath.getValueSafe());
            } else if (result.equals(options.get(2))) {
                cmd = Client.getTempReadOnlyURL(simpleMyFileProperty.getId(),currentPath.getValueSafe());
            } else if (result.equals(options.get(3))) {
                cmd = Client.getTempReadAndWriteURL(simpleMyFileProperty.getId(),currentPath.getValueSafe());
            }
            System.out.println(cmd);
        }
    }

    public void shareJump(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        //shareListChooser.
        shareIdStack.clear();;
        curShareURL = shareListChooser.getValue();
        shareIdStack.push(Long.parseLong(curShareURL.split(":")[1]));
        shareFlush();
    }

    public void shareAdd(ActionEvent actionEvent) {
        String list = MyDialog.showTextInputDialog("输入共享链接");
        if(list != null){
            shareListChooser.getItems().add(list);
        }
    }

    public void shareBack(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(shareIdStack.size()==1)return;
        shareIdStack.pop();
        shareFlush();
    }

    public void shareDownload(ActionEvent actionEvent) {

    }

    public void shareRename(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

    }

    public void shareRemove(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(shareMyFileProperty == null)return;
        try {
            if (MyDialog.showCheckAlert("是否确定删除")) {
                boolean flag = false;

                if (shareMyFileProperty.getType().equals("文件夹"))
                    flag = deleteFileDirectory(shareIdStack.peek(),curShareURL, shareMyFileProperty.getName());
                else
                    flag = deleteFile(shareIdStack.peek(),curShareURL, shareMyFileProperty.getName());
                //flush();
                shareFlush();
                //if (flag) {
                Log.log("删除" + curShareURL + shareMyFileProperty.getName() + (flag ? "成功" : "失败"));
                //}
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void shareBind(ActionEvent actionEvent) {

    }

    public void shareNewFileDirectory(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String text = MyDialog.showTextInputDialog("输入文件夹名称");
        boolean flag = false;
        try {
            Client.createFileDirectory(shareIdStack.peek(),curShareURL, text);
            flag = true;
        } catch (NoUserException | NoAccessException | NoFileException | FileExistedException e) {
            e.printStackTrace();
        }
        Log.log("创建" + currentPath.getValueSafe() + "/" + text + (flag ? "成功" : "失败"));
        shareFlush();
    }
}
