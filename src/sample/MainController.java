package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private ListView<String> list_Files;
    @FXML
    private Label label_username;
    @FXML
    private Circle circle_userImage;

    @FXML
    public void initialize(){
        label_username.setText(Main.name);
        list_Files.setCellFactory(param -> new FileListCell());
    }

    @FXML
    private void Button_UploadFiles_Click(ActionEvent event){
        System.out.println("UploadFiles Clicked!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        List<File> fileList =  fileChooser.showOpenMultipleDialog(Main.stage);
        List<String> fileNames = new ArrayList();
        //list_files_items.
        if(fileList!=null)
        {
            for(File file : fileList) {
                System.out.println(file.getName());
                fileNames.add(file.getName());
            }
        }
        list_Files.setItems(FXCollections.observableList(fileNames));
    }

    @FXML
    private void Button_UploadFolder_Click(ActionEvent event){
        System.out.println("UploadFolder Clicked!");
    }

    @FXML
    private void Button_Send_Click(ActionEvent event){
        System.out.println("Send Clicked!");
    }

    static class FileListCell extends ListCell<String>
    {
        HBox hbox = new HBox();
        Image icon = new Image("File:txtFile.png");
        ImageView  iconView = new ImageView(icon);
        Label fileName = new Label("File name");
        Pane pane = new Pane();
        Button btn = new Button();

        public FileListCell(){
            super();
            btn.setText("remove");
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
            hbox.getChildren().addAll(iconView,fileName,pane,btn);
            hbox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(String name, boolean empty){

            super.updateItem(name,empty);
            setText(null);
            setGraphic(null);
            if(name!=null && !empty)
            {
                fileName.setText(name);
                setGraphic(hbox);
            }
        }
    }
}