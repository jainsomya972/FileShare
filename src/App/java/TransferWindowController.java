package App.java;

import javafx.fxml.FXML;
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

    public static double progressValue=0;

    @FXML
    public void initialize()
    {
        if(Main.dialogStage!=null)
            Main.dialogStage.close();
        if (Main.sendType.equals("files")) {
            //SendFile(selectedIP, fileToSend);
            SendFile sendFile = new SendFile(selectedIP, fileList);
            Thread t = new Thread(sendFile);
            t.start();
        } else {
            //SendFolder(selectedIP, fileToSend);
            SendFolder sendFolder = new SendFolder(selectedIP, fileList, transfer_progressbar, transfer_percent_label, transfer_progress_label);
            Thread t = new Thread(sendFolder);
            t.start();
        }

    }


}
