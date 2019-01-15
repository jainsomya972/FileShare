package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DiscoveryDialogController {

    @FXML
    ListView listView_Discovery;

    @FXML
    public  void initialize(){
        FutureTask f;
        Callable scan = new ScanNetwork(6700);
        f=new FutureTask(scan);
        Thread t = new Thread(f);
        t.start();


        try {
            listView_Discovery.setItems(FXCollections.observableList((List)f.get()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }





    }

    public DiscoveryDialogController(){

    }


}
