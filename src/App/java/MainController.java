package App.java;

import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.jfoenix.controls.JFXToggleButton;

import java.io.*;
import java.net.ServerSocket;
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
    private JFXToggleButton toggleButton_discoverable;
    @FXML
    private JFXSpinner receive_progress;
    @FXML
    private Label label_sender_name, label_data_size;

    public static File folder;

    @FXML
    public void initialize() {
        Main.discoveryClient = new DiscoveryClient(6700,Main.name, receive_progress, label_data_size, label_sender_name);
        new Thread(Main.discoveryClient).start();
        String imagePath = Paths.get("/App/images/user_image.png").toAbsolutePath().toString();
        Image i = null;
        try {
            i = new Image(new FileInputStream(getClass().getResource("/App/images/user_image.png").getPath()));
            circle_userImage.setFill(new ImagePattern(i));
            System.out.println(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        folder = directoryChooser.showDialog(Main.stage);
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
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/App/fxml/discovery.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Available Devices");
            primaryStage.setScene(new Scene(root, 350, 450));
            /*primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);*/
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    @FXML
    private void ToggleButton_Discoverable_Click(ActionEvent event) {

            if (toggleButton_discoverable.isSelected()){
                System.out.println("Socket opened!!");
                Main.discoveryClient.OpenServerSocket();
            }
            else {
                Main.discoveryClient.CloseServerSocket();
                System.out.println("Socket closed!!");
            }

    }
}