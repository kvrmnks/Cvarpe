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
    public TableColumn<SimpleMyFileProperty, String> shareListTypeColumn;
    public TableColumn<SimpleMyFileProperty, String> shareListNameColumn;
    public TableColumn<SimpleMyFileProperty, String> shareListSizeColumn;
    public TableColumn<SimpleMyFileProperty, String> shareListTimeColumn;
    public TableView<SimpleMyFileProperty> shareListTableView;
    public MenuItem shareOpen;
    public MenuItem shareDownload;
    public MenuItem shareRename;
    public MenuItem shareRemove;
    public MenuItem shareBind;
    public MenuItem shareNewDirectory;
    public TableColumn<SimpleLogListProperty, String> logListSpeedTableColumn;
    public Button logListBegin;
    public Button logListPause;
    public MenuItem shareUpload;
    public ContextMenu shareContextMenu;
    public ContextMenu logMenu;
    public TableColumn<SimpleStringProperty, String> logConditionTableColumn;
    private Main application;
    private ObservableList<SimpleMyFileProperty> data = FXCollections.observableArrayList();
    private ObservableList<SimpleMyFileProperty> searchResult = FXCollections.observableArrayList();
    private ObservableList<SimpleLogListProperty> logdata = FXCollections.observableArrayList();
    private ObservableList<SimpleMyFileProperty> shareList = FXCollections.observableArrayList();
    private SimpleMyFileProperty simpleMyFileProperty, shareMyFileProperty;
    private SimpleStringProperty currentPath;
    private Stack<Long> idStack = new Stack<>();
    private Stack<Long> idDirectoryStack = new Stack<>();
    private Stack<Long> shareIdStack = new Stack<>();
    private Stack<Long> shareDirectoryStack = new Stack<>();
    private String curShareURL = "";
    private SimpleLogListProperty curLogListProperty;
    private YRLList yrlList;

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
                                upload(file);
                            } else {
                                try {
                                    System.out.println("fake");
                                    uploadFileDirectory(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    //  for (File file : db.getFiles()) {
                    //    filePath = file.getAbsolutePath();
                    //System.out.println(filePath);
                    //}
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
        logListSpeedTableColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        modifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        logConditionTableColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        modifyProcessTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator>, ObservableValue<ProgressIndicator>>() {
            @Override
            public ObservableValue<ProgressIndicator> call(TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator> param) {
                return new SimpleObjectProperty<ProgressIndicator>(param.getValue().getProgressBar());
            }
        });
        logTableView.setRowFactory(param -> {
            TableRow<SimpleLogListProperty> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2){
                    TableRow<SimpleLogListProperty> r = (TableRow<SimpleLogListProperty>) event.getSource();
                    SimpleLogListProperty simpleLogListProperty = r.getItem();
                    if(simpleLogListProperty == null);
                    else{
                        if(simpleLogListProperty.currentCondition()){
                            simpleLogListProperty.start();
                        }
                        else{
                            simpleLogListProperty.stop();
                        }
                    }
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    logMenu.show(logTableView, event.getScreenX(), event.getScreenY());
                    TableRow<SimpleLogListProperty> r = (TableRow<SimpleLogListProperty>) event.getSource();
                    setCurLogListProperty(r.getItem());
                }
            });
            return row;
        });

        logTableView.setItems(logdata);
    }

    private void initShareTab() {
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
                                shareOpen(null);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            shareContextMenu.show(fileTableView, event.getScreenX(), event.getScreenY());
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            setShareMyFileProperty(r.getItem());
                        }
                    }
                });

                return row;
            }
        });
    }

    private void initLogList() {
        yrlList = YRLList.getByUserName(UserData.getUserName());
        YRL[] yrls = yrlList.toArray();
        for (YRL x : yrls) {
            logdata.add(new SimpleLogListProperty(x));
        }
        transferBegin(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPath = new SimpleStringProperty("Editor^0^/" + UserData.getUserName() + "/");
        initFileTab();
        initSearchTab();
        initLogTab();
        initLogList();
        initShareTab();
    }

    private void setSimpleMyFileProperty(SimpleMyFileProperty viewMyFile) {
        this.simpleMyFileProperty = viewMyFile;
    }

    private void setShareMyFileProperty(SimpleMyFileProperty myFileProperty) {
        this.shareMyFileProperty = myFileProperty;
    }

    private void setCurLogListProperty(SimpleLogListProperty simpleLogListProperty) {
        this.curLogListProperty = simpleLogListProperty;
    }

    public void setApp(Main app) {
        application = app;
    }

    public void init() {
        try {
            idStack.push(Client.getStructure(currentPath.getValueSafe()).getId());
            idDirectoryStack.push(idStack.peek());
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
        assert file != null;
        MyFile[] files = file.getCurrentFileList();
        for (MyFile mf : files) {
            data.add(new SimpleMyFileProperty(mf));
        }
    }

    public void shareFlush() throws IOException, ClassNotFoundException {
        shareList.clear();
        MyFile file = null;
        try {
            file = Client.getStructure(shareIdStack.peek(), curShareURL);
        } catch (NoUserException | NoAccessException | NoFileException e) {
            e.printStackTrace();
        }
        // data.add(new SimpleMyFileProperty(file));
        assert file != null;
        MyFile[] files = file.getCurrentFileList();
        for (MyFile mf : files) {
            shareList.add(new SimpleMyFileProperty(mf));
        }
    }

    public void open(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (simpleMyFileProperty == null) return;
        if (simpleMyFileProperty.getId().equals(idStack.peek())) return;
        currentPath.setValue(currentPath.getValueSafe() + simpleMyFileProperty.getName() + "/");
        idStack.push(simpleMyFileProperty.getId());
        if (simpleMyFileProperty.getType().equals("文件夹"))
            idDirectoryStack.push(simpleMyFileProperty.getId());
        flush();
    }

    public void shareOpen(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (shareMyFileProperty == null) return;
        if (shareMyFileProperty.getId().equals(shareIdStack.peek())) return;
        shareIdStack.push(shareMyFileProperty.getId());
        shareFlush();
    }

    public void download(ActionEvent actionEvent) throws IOException, ClassNotFoundException, NoUserException, NoAccessException, NoFileException, NotBoundException {
        if (simpleMyFileProperty == null) return;
        if (simpleMyFileProperty.getType().equals("文件夹")) return;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载目录");
        File f = directoryChooser.showDialog(application.getStage());
        if (f == null) return;
        YRL yrl = new YRL(
                idDirectoryStack.peek()
                , currentPath.getValueSafe()
                , simpleMyFileProperty.getName()
                , f.getAbsolutePath() + "/" + simpleMyFileProperty.getName()
                , YRL.TYPE_DOWNLOAD
                , MyDate.getCurTime()
                , simpleMyFileProperty.getRsize()
        );
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(yrl);
        logdata.add(logListProperty);
        yrlList.add(yrl);
        logListProperty.start();
        //Client.downLoad(idDirectoryStack.peek(), currentPath.getValueSafe(), simpleMyFileProperty.getName(), f.getAbsolutePath() + "/" + simpleMyFileProperty.getName(), logListProperty);
    }

    private void reNameFile(long id, String pos, String name, String newName) throws IOException {
        try {
            Client.reNameFile(id, pos, name, newName);
            Log.log(MyDate.getCurTime() + "修改文件名成功");
            Log.log(pos + name + " -> " + pos + newName);
        } catch (ClassNotFoundException | NoAccessException | NoUserException | NoFileException | FileExistedException e) {
            Log.log("修改文件名失败");
            e.printStackTrace();
        }

    }

    private void reNameFileDirectory(long id, String pos, String name, String newName) throws IOException {
        try {
            Client.reNameFileDirectory(id, pos, name, newName);
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
                reNameFileDirectory(idStack.peek(), currentPath.getValueSafe(), simpleMyFileProperty.getName(), newName);
            } else {
                reNameFile(idStack.peek(), currentPath.getValueSafe(), simpleMyFileProperty.getName(), newName);
            }
            flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteFile(long id, String pos, String name) {
        try {
            Client.deleteFile(id, pos, name);
            return true;
        } catch (ClassNotFoundException | NoUserException | NoAccessException | NoFileException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteFileDirectory(long id, String pos, String name) throws IOException {
        try {
            Client.deleteFileDirectory(id, pos, name);
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
                    flag = deleteFileDirectory(idStack.peek(), currentPath.getValueSafe(), simpleMyFileProperty.getName());
                else
                    flag = deleteFile(idStack.peek(), currentPath.getValueSafe(), simpleMyFileProperty.getName());
                flush();
                Log.log("删除" + currentPath.getValueSafe() + simpleMyFileProperty.getName() + (flag ? "成功" : "失败"));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void binding(ActionEvent actionEvent) {
    }

    public void backForward(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (idStack.size() == 1) {
            flush();
            return;
        }
        while (idDirectoryStack.size() > 1 && idStack.size() > 1 && idDirectoryStack.peek().equals(idStack.peek())) {
            idStack.pop();
            idDirectoryStack.pop();
        }
        currentPath.setValue(MyFile.backFroward(currentPath.getValueSafe()));
        idStack.pop();
        while (idDirectoryStack.size() > 1 && idStack.size() > 1 && idDirectoryStack.peek().equals(idStack.peek())) {
            idStack.pop();
            idDirectoryStack.pop();
        }
        flush();
    }

    private void upload(File f) {
        YRL yrl = new YRL(
                idDirectoryStack.peek()
                , currentPath.getValueSafe()
                , f.getName()
                , f.getAbsolutePath()
                , YRL.TYPE_UPLOAD
                , MyDate.getCurTime()
                , f.length());
        yrlList.add(yrl);
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(yrl);
        logdata.add(logListProperty);
        logListProperty.start();
    }

    public void upload(ActionEvent actionEvent) throws IOException, ClassNotFoundException, InterruptedException, NoUserException, NoAccessException, NoFileException, FileStructureException, NotBoundException, FileExistedException {
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
            Client.createFileDirectory(idStack.peek(), currentPath.getValueSafe(), text);
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
        if (simpleMyFileProperty == null) return;
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
                cmd = Client.getReadOnlyURL(simpleMyFileProperty.getId(), idDirectoryStack.peek(), currentPath.getValueSafe());
            } else if (result.equals(options.get(1))) {
                cmd = Client.getReadAndWriteURL(simpleMyFileProperty.getId(), idDirectoryStack.peek(), currentPath.getValueSafe());
            } else if (result.equals(options.get(2))) {
                cmd = Client.getTempReadOnlyURL(simpleMyFileProperty.getId(), idDirectoryStack.peek(), currentPath.getValueSafe());
            } else if (result.equals(options.get(3))) {
                cmd = Client.getTempReadAndWriteURL(simpleMyFileProperty.getId(), idDirectoryStack.peek(), currentPath.getValueSafe());
            }
            MyDialog.showInputDialog("链接", "创建链接", "链接", cmd);
            Log.log("创建链接接成功\n" + cmd);
            //System.out.println(cmd);
        }

    }

    public void shareJump(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        shareIdStack.clear();
        ;
        curShareURL = shareListChooser.getValue();
        shareIdStack.push(Long.parseLong(curShareURL.split(":")[1]));
        shareDirectoryStack.push(Long.parseLong(curShareURL.split(":")[2]));
        shareFlush();
    }

    public void shareAdd(ActionEvent actionEvent) {
        String list = MyDialog.showTextInputDialog("输入共享链接");
        if (list != null) {
            shareListChooser.getItems().add(list);
        }
    }

    public void shareBack(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (shareIdStack.size() == 1) return;
        while (shareIdStack.size() > 1 && shareDirectoryStack.size() > 1 && shareDirectoryStack.peek().equals(shareIdStack.peek())) {
            shareIdStack.pop();
            shareDirectoryStack.pop();
        }
        shareIdStack.pop();
        while (shareIdStack.size() > 1 && shareDirectoryStack.size() > 1 && shareDirectoryStack.peek().equals(shareIdStack.peek())) {
            shareIdStack.pop();
            shareDirectoryStack.pop();
        }
        shareFlush();
    }

    public void shareDownload(ActionEvent actionEvent) throws NoUserException, IOException, NoAccessException, NotBoundException, NoFileException, ClassNotFoundException {
        if (shareMyFileProperty == null) return;
        if (shareMyFileProperty.getType().equals("文件夹")) return;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载目录");
        File f = directoryChooser.showDialog(application.getStage());
        if (f == null) return;
        YRL yrl = new YRL(
                shareDirectoryStack.peek()
                , curShareURL
                , shareMyFileProperty.getName()
                , f.getAbsolutePath() + "/" + shareMyFileProperty.getName()
                , YRL.TYPE_DOWNLOAD
                , MyDate.getCurTime()
                , shareMyFileProperty.getRsize()
        );
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(yrl);
        yrlList.add(yrl);
        logdata.add(logListProperty);
        logListProperty.start();
    }

    public void shareUploadFile(ActionEvent actionEvent) throws NoUserException, IOException, NoFileException, FileStructureException, FileExistedException, NotBoundException, ClassNotFoundException, NoAccessException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要上传的文件");
        File f = fileChooser.showOpenDialog(application.getStage());
        if (f == null)
            return;
        YRL yrl = new YRL(
                shareDirectoryStack.peek()
                , curShareURL
                , f.getName()
                , f.getAbsolutePath()
                , YRL.TYPE_UPLOAD
                , MyDate.getCurTime()
                , f.length()
        );
        yrlList.add(yrl);
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(yrl);
        logdata.add(logListProperty);
        logListProperty.start();
        Thread.sleep(500);
        shareFlush();
    }

    public void shareRename(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (shareMyFileProperty == null) return;
        try {
            String newName = MyDialog.showTextInputDialog("输入新的文件名");
            if (newName == null || newName.equals("")) return;
            if (shareMyFileProperty.getType().equals("文件夹")) {
                reNameFileDirectory(shareDirectoryStack.peek(), curShareURL, shareMyFileProperty.getName(), newName);
            } else {
                reNameFile(shareDirectoryStack.peek(), curShareURL, shareMyFileProperty.getName(), newName);
            }
            flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shareRemove(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (shareMyFileProperty == null) return;
        try {
            if (MyDialog.showCheckAlert("是否确定删除")) {
                boolean flag = false;
                if (shareMyFileProperty.getType().equals("文件夹"))
                    flag = deleteFileDirectory(shareDirectoryStack.peek(), curShareURL, shareMyFileProperty.getName());
                else
                    flag = deleteFile(shareDirectoryStack.peek(), curShareURL, shareMyFileProperty.getName());
                shareFlush();
                Log.log("删除" + curShareURL + shareMyFileProperty.getName() + (flag ? "成功" : "失败"));
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
            Client.createFileDirectory(shareIdStack.peek(), curShareURL, text);
            flag = true;
            Log.log("创建" + curShareURL + "/" + text + (flag ? "成功" : "失败"));
            shareFlush();
            flush();
        } catch (NoUserException | NoAccessException | NoFileException | FileExistedException e) {
            e.printStackTrace();
        }
    }

    public void transferPause(ActionEvent actionEvent) {
        SimpleLogListProperty[] ret = new SimpleLogListProperty[logdata.size()];
        logdata.toArray(ret);
       // logdata.clear();
        for (SimpleLogListProperty x : ret) {
            x.stop();
           // logdata.add(x);
        }
    }

    public void transferBegin(ActionEvent actionEvent) {
        SimpleLogListProperty[] ret = new SimpleLogListProperty[logdata.size()];
        logdata.toArray(ret);
       // logdata.clear();
        for (SimpleLogListProperty x : ret) {
            if (!x.isFinished()) x.start();
         //   logdata.add(x);
        }
    }


    public void logPause(ActionEvent actionEvent) {
        if (curLogListProperty == null) return;
        curLogListProperty.stop();
    }

    public void logStart(ActionEvent actionEvent) {
        if (curLogListProperty == null) return;
        curLogListProperty.start();
    }

    public void logDelete(ActionEvent actionEvent) {
        if (curLogListProperty == null) return;
        logdata.remove(curLogListProperty);
        yrlList.remove(curLogListProperty.getYrl());
    }

    public void logProperty(ActionEvent actionEvent) {
        if (curLogListProperty == null) return;
    }
}
