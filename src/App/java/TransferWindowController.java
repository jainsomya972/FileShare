package App.java;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

import static App.java.Main.selectedIP;
import static App.java.Main.fileList;

public class TransferWindowController {

    @FXML
    private ProgressBar transfer_progressbar;
    @FXML
    private Label transfer_percent_label, transfer_progress_label;
    @FXML
    private JFXListView<String> listview_file;

    public static double progressValue=0;

    @FXML
    public void initialize()
    {
        listview_file.setCellFactory(param -> new FileListCell());
        if(Main.dialogStage!=null)
            Main.dialogStage.close();
        if (Main.sendType.equals("files")) {
            //SendFile(selectedIP, fileToSend);
            SendFile sendFile = new SendFile(selectedIP, fileList, transfer_progressbar, transfer_percent_label, transfer_progress_label, listview_file);
            Thread t = new Thread(sendFile);
            t.start();
        } else {
            //SendFolder(selectedIP, fileToSend);
            SendFolder sendFolder = new SendFolder(selectedIP, fileList, transfer_progressbar, transfer_percent_label, transfer_progress_label, listview_file);
            Thread t = new Thread(sendFolder);
            t.start();
        }

    }


    @FXML
    private void Button_Finish_Click(ActionEvent event){
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/App/fxml/main.fxml"));
            Main.stage.setTitle("FileJet - Home");
            Main.stage.setScene(new Scene(root,1080,600));
            Main.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
