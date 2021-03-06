package App.java;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class NewUserDialogController {
    @FXML
    JFXTextField textField_name;

    @FXML
    JFXTextField textField_email;

    @FXML
    Circle circle_image;

    String name;
    String email;
    String imagePath="";

    protected void Success_Function(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("config.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(name);
        writer.println(email);
        writer.println(imagePath);
        writer.close();

        Main.name = name;
        Main.email = email;
        Main.profileImage = imagePath;

        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/App/fxml/main.fxml"));
            Main.stage.setTitle("FileJet - Home");
            Main.stage.setScene(new Scene(root, 800, 600));
            Main.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Button_Register_Click(ActionEvent event){
        name = textField_name.getText();
        email = textField_email.getText();
        System.out.println("name= "+name);

        if(imagePath==""){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Image not uploaded");
            alert.setHeaderText(null);
            alert.setContentText("Please upload an image first to continue");
            alert.showAndWait();
        }
        else if(name.isEmpty() || email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the entries");
            alert.showAndWait();
        }
        else{
            Success_Function();

        }

    }

    @FXML
    private void Button_Upload_Click(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File image =  fileChooser.showOpenDialog(Main.stage);
        Path source = Paths.get(String.valueOf(image)); //original file
        System.out.println(source);
        Path target = Paths.get("src/App/images/");

        Path t = target.resolve("user_image.png");// create new path ending with `name` content

        Image i = null;
        try {
            Files.copy(source, t, StandardCopyOption.REPLACE_EXISTING);
            imagePath = Paths.get("src/App/images/user_image.png").toAbsolutePath().toString();
            i = new Image(new FileInputStream(getClass().getResource("/App/images/user_image.png").getPath()));
            circle_image.setFill(new ImagePattern(i));
            System.out.println(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
