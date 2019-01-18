package App.java;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DiscoveryDialogController {

    @FXML
    private ListView listView_Discovery;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private JFXButton button_send;

    @FXML
    public  void initialize(){
        progressIndicator.setVisible(true);
        button_send.setDisable(true);
        ScanNetwork scan = new ScanNetwork(6700,this);
        Thread t = new Thread(scan);
        t.start();
    }

    @FXML
    public void Button_Refresh_Click(){
        progressIndicator.setVisible(true);
        button_send.setDisable(true);
        ScanNetwork scan = new ScanNetwork(6700,this);
        Thread t = new Thread(scan);
        t.start();
    }

    @FXML
    public void Button_Send_Click(){

    }

    public DiscoveryDialogController(){

    }

    public void OnScanCompletion(List<String> users) {
        listView_Discovery.setItems(FXCollections.observableList(users));
        progressIndicator.setVisible(false);
    }

}
