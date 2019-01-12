package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainController {


    @FXML
    private ListView<String> list_Files;
    @FXML
    private Label label_username;
    @FXML
    private Circle circle_userImage;
    @FXML
    private Button button_send;
    @FXML
    private AnchorPane anchorPane_base;

    @FXML
    public void initialize() throws FileNotFoundException {
        label_username.setText(Main.name);
        list_Files.setCellFactory(param -> {
            try {
                return new FileListCell();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @FXML
    private void Button_UploadFiles_Click(ActionEvent event){
        System.out.println("UploadFiles Clicked!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Files");
        Main.fileList =  fileChooser.showOpenMultipleDialog(Main.stage);
        List<String> fileNames = new ArrayList();
        //list_files_items.
        if(Main.fileList!=null)
        {
            for(File file : Main.fileList) {
                System.out.println(file.getName());
                fileNames.add(file.getName());
            }
            list_Files.setItems(FXCollections.observableList(fileNames));
            button_send.setDisable(false);
        }
    }

    @FXML
    private void Button_UploadFolder_Click(ActionEvent event){
        System.out.println("UploadFolder Clicked!");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Folder");

    }

    @FXML
    private void Button_Send_Click(ActionEvent event){
        System.out.println("Send Clicked!");
    }
}