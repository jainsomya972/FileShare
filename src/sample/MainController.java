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
import javafx.scene.shape.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

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
    public void initialize() {
        label_username.setText(Main.name);
        list_Files.setCellFactory(param -> new FileListCell());
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
            Main.sendType = "files";
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
        File folder = directoryChooser.showDialog(Main.stage);
        List<String> folder_list = new ArrayList<String>();
        if(folder!=null)
        {
            Main.sendType = "folder";
            Main.fileList = enlistFolder(folder);
            writeFilePathsToFile(Main.fileList, "files.tmp", folder);
            folder_list.add(folder.getName() + ".dir");
            list_Files.setItems(FXCollections.observableList(folder_list));
            button_send.setDisable(false);
        }

    }

    @FXML
    private void Button_Send_Click(ActionEvent event){
        System.out.println("Send Clicked!");
    }

    private List<File> enlistFolder(File folder)
    {
        List<File> files = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();
        queue.add(folder);
        File temp;
        while(!queue.isEmpty()) {
            temp = queue.remove();
            for(File f : Objects.requireNonNull(temp.listFiles()))
            {
                if(f.isDirectory())
                    queue.add(f);
                else
                    files.add(f);
            }
        }
        return files;
    }

    private void writeFilePathsToFile(List<File> files, String configFileName, File folder)
    {
        File configFile = new File(configFileName);
        String parentPath = folder.getParent();
        System.out.println("Redundant path" + parentPath);
        try {
            System.out.println("Output File : " + configFile.getCanonicalPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            for(File file : files){
                writer.write(file.getCanonicalPath().replace(parentPath,""));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}