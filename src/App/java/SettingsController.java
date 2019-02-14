package App.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SettingsController {

    @FXML
    private void Button_Back_Click(ActionEvent event){
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/App/fxml/main.fxml"));
            Main.stage.setTitle("FileJet - Home");
            Main.stage.setScene(new Scene(root, Main.stage.getWidth(), Main.stage.getHeight()));
            Main.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
