package App.java;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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

    private List<String> ips = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private String nameSelected;
    private List<File> fileToSend;
    private int port=6700;

    private File folder;

    @FXML
    public  void initialize(){
        progressIndicator.setVisible(true);
        button_send.setDisable(true);
        folder = MainController.folder;
        ScanNetwork scan = new ScanNetwork(port,this);
        Thread t = new Thread(scan);
        t.start();
        ListViewSelected();
    }

    @FXML
    public void Button_Refresh_Click(){
        progressIndicator.setVisible(true);
        button_send.setDisable(true);
        ScanNetwork scan = new ScanNetwork(port,this);
        Thread t = new Thread(scan);
        t.start();
    }

    @FXML
    public void Button_Send_Click(){
        int index=users.indexOf(nameSelected);
        String selectedIP = ips.get(index);
        System.out.println("Selected IP: "+selectedIP);
        fileToSend = Main.fileList;
        String message="receiveconfirm "+Main.name+"\n";
        String receivedMessage = SendFileForConfirmation(selectedIP,message);

        if(receivedMessage.equals("yes")){
            if(Main.sendType.equals("files")) {
                //SendFile(selectedIP, fileToSend);
                SendFile sendFile = new SendFile(selectedIP,fileToSend);
                Thread t = new Thread(sendFile);
                t.start();
            }
            else {
                //SendFolder(selectedIP, fileToSend);
                SendFolder sendFolder = new SendFolder(selectedIP,fileToSend);
                Thread t = new Thread(sendFolder);
                t.start();
            }
        }
        else{
            System.out.println("Files will not transfer. Denied by user.");
        }


    }



    public DiscoveryDialogController(){

    }

    public void OnScanCompletion(List<String> users,List<String> ips) {
        this.ips = ips;
        this.users = users;
        listView_Discovery.setItems(FXCollections.observableList(users));
        progressIndicator.setVisible(false);
    }

    private void ListViewSelected(){
        listView_Discovery.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ip, Object oldValue, Object newValue) {
                button_send.setDisable(false);
                nameSelected=(String)newValue;
            }
        });
    }

    private String SendFileForConfirmation(String receiver,String m){
        Socket sendToServer=null;
        String message;
        try {
            sendToServer = new Socket(receiver,port);
            //Send the message to the server
            OutputStream os = sendToServer.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write(m);
            bw.flush();
            System.out.println("Message sent to the server : "+m);



            //Get the return message from the server
            InputStream is = sendToServer.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            message = br.readLine();
            System.out.println("Message received from the server : " +message);
            sendToServer.close();
            return message;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "no";
    }


}
