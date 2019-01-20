package App.java;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoveryClient implements Runnable{

    public static ServerSocket socket;
    private Socket fromClient;
    private String name;
    private static int port;

    public DiscoveryClient(int port, String name){
        this.name=name;
        this.port = port;
        try{
            socket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        //Server is running always. This is done using this while(true) loop

            while (true) {
                //Reading the message from the client
                if(!socket.isClosed()) {
                try {
                    fromClient = socket.accept();
                    InputStream is = fromClient.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String inputMessage = br.readLine();
                    HandleInputMessage(inputMessage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void OpenServerSocket(){
        if(socket.isClosed()){
            try {
                socket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void CloseServerSocket(){
        if(!socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void HandleInputMessage(String inputMessage) throws IOException {

        System.out.println("Message received from client is " + inputMessage);


        if(inputMessage.equals("getname"))
        {
            String returnMessage = "unknown";
            returnMessage = Main.name + "\n";
            //Sending the response back to the client.
            OutputStream os = fromClient.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(returnMessage);
            System.out.println("Message sent to the client is " + returnMessage);
            bw.flush();
        }
        else if(inputMessage.contains("receiveconfirm"))
        {
            String name = inputMessage.split(" ")[1];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String returnMessage = "unknown";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            name + " is sending some Files, do you want to receive it?",
                            ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES)
                        returnMessage = "yes" + "\n";
                    else
                        returnMessage = "no" + "\n";
                    OutputStream os = null;
                    try {
                        os = fromClient.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(returnMessage);
                        System.out.println("Message sent to the client is " + returnMessage);
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else if(inputMessage.equals("filetransfer"))
        {

        }
        else if(inputMessage.equals("foldertransfer"))
        {

        }
    }

    /*public static boolean ServerSocketIsClosed(){
        if(socket.isClosed()){
            return true;
        }
        return false;
    }*/

}
