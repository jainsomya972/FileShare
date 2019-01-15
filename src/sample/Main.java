package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    public static Stage stage;
    public static String name = "Abhay";
    public static String email = "jainsomya972@gmail.com";
    public static String profileImage;
    public static List<File> fileList;
    public static String sendType = "files";// can be files or folder
    @Override
    public void start(Stage primaryStage) throws Exception{
        File userFile = new File(Paths.get("config.txt").toString());
        BufferedReader reader = null;
        Parent root;
        if(userFile.exists()) {
            reader = new BufferedReader(new FileReader(userFile));
            name = reader.readLine();
            email = reader.readLine();
            profileImage = reader.readLine();
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        }
        else
            root = FXMLLoader.load(getClass().getResource("new_user_dialog.fxml"));

        stage = primaryStage;
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
