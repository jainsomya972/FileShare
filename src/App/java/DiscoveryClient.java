package App.java;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DiscoveryClient implements Runnable{

    public static ServerSocket socket;
    private Socket fromClient;
    private String name;
    private String senderName;
    private static int port;
    private File folder;
    public JFXSpinner progress;
    public Label label_data_size, label_sender_name;
    private int received=0;
    private long totalLength = 0;
    private long receivedLength = 0;

    public DiscoveryClient(int port, String name, JFXSpinner progress, Label label_data_size, Label label_sender_name){
        this.name=name;
        this.port = port;
        this.progress = progress;
        this.label_data_size = label_data_size;
        this.label_sender_name = label_sender_name;
        try{
            socket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

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
                    DataInputStream dis = new DataInputStream(is);
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String inputMessage = br.readLine();
                    HandleInputMessage(inputMessage,br,dis);


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
    private void HandleInputMessage(String inputMessage, BufferedReader br, DataInputStream dis) throws IOException {

        System.out.println("Message received from client is " + inputMessage);


        if (inputMessage.equals("getname")) {
            String returnMessage = "unknown";
            returnMessage = Main.name + "\n";
            //Sending the response back to the client.
            OutputStream os = fromClient.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(returnMessage);
            System.out.println("Message sent to the client is " + returnMessage);
            bw.flush();
        } else if (inputMessage.contains("receiveconfirm")) {
            senderName = inputMessage.split(" ")[1];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String returnMessage = "unknown";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            senderName + " is sending some Files, do you want to receive it?",
                            ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Open Folder");
                        folder = directoryChooser.showDialog(Main.stage);
                        if (folder != null) {
                            returnMessage = "yes" + "\n";
                        } else
                            returnMessage = "no" + "\n";
                    } else
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

        } else if (inputMessage.equals("filetransfer")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progress.setVisible(true);
                    label_sender_name.setVisible(true);
                    label_data_size.setVisible(true);
                }
            });

            int fileCount = Integer.parseInt(br.readLine());
            System.out.println("Files Count : " + fileCount);

            ArrayList<String> fileNames = new ArrayList<>();
            ArrayList<Integer> fileLengths = new ArrayList<>();
            totalLength=0;
            receivedLength=0;
            for (int count = 0; count < fileCount; count++) {
                fileNames.add(br.readLine());
                fileLengths.add(Integer.parseInt(br.readLine()));
                totalLength+=fileLengths.get(count);
                System.out.println(count + "  " + fileNames.get(count)+ "  "+fileLengths.get(count));
            }

            for (int count = 0; count < fileCount; count++) {
                /*String fileName = dis.readUTF();
                int length = Integer.parseInt(fileName.split("\n")[1]);
                fileName = fileName.split("\n")[0];
                System.out.println("File Name : " + fileName);
                System.out.println("Length : " + length);
                System.out.println("File Data : ");
                File file = new File(folder.getPath() +"/"+ fileName);
*/
                //file.mkdirs();
                int length = fileLengths.get(count);
                File file = new File(folder.getPath()+"/"+fileNames.get(count));
                FileOutputStream fos = new FileOutputStream(file);

                received = 0;
                int c;
                byte[] buffer = new byte[65536];
                while (received < length && (c = dis.read(buffer, 0, Math.min(buffer.length, length - received))) > 0) {
                    fos.write(buffer, 0, c);
                    //fos.flush();
                    received += c;
                    receivedLength+=c;
                    if(progress!=null) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                label_sender_name.setText("Receiving from " + senderName + " ...");
                                label_data_size.setText(GetDataTransferString());
                                progress.setProgress(receivedLength / (double) totalLength);
                            }
                        });
                    }
                }

                //fos.write(item);
                fos.close();
                System.out.println("\nFile received successfully!!! voila !! :)");
                //br.readLine();
            }
        } else if (inputMessage.equals("foldertransfer")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progress.setVisible(true);
                    label_sender_name.setVisible(true);
                    label_data_size.setVisible(true);
                }
            });
            int fileCount = Integer.parseInt(br.readLine());
            System.out.println("Files Count : " + fileCount);
            ArrayList<String> fileNames = new ArrayList<>();
            ArrayList<Integer> fileLengths = new ArrayList<>();
            totalLength=0;
            receivedLength=0;
            for (int count = 0; count < fileCount; count++) {
                fileNames.add(br.readLine());
                fileLengths.add(Integer.parseInt(br.readLine()));
                totalLength+=fileLengths.get(count);
                System.out.println(count + "  " + fileNames.get(count)+ "  "+fileLengths.get(count));
            }

            for (int count = 0; count < fileCount; count++) {

                int length = fileLengths.get(count);
                File file = new File(folder.getPath()+fileNames.get(count));
                String[] paths = file.getPath().split("/");
                String path = paths[paths.length-1];
                path = (folder.getPath()+fileNames.get(count)).replace(path,"");

                new File(path).mkdirs();
                //file.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);

                received = 0;
                int c;
                byte[] buffer = new byte[65536];
                double lengthProgress = 0;
                while (received < length && (c = dis.read(buffer, 0, Math.min(buffer.length, length - received))) > 0) {
                    fos.write(buffer, 0, c);
                    //fos.flush();
                    received += c;
                    receivedLength+=c;
                    if(progress!=null) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                label_sender_name.setText("Receiving from " + senderName + " ...");
                                label_data_size.setText(GetDataTransferString());
                                progress.setProgress(receivedLength/(double)totalLength);
                            }
                        });
                    }
                }

                fos.close();
                System.out.println("\nFile received successfully!!! voila !! :)");

            }
            System.out.print("Folder received successfully");
        }


    }
    private String GetDataTransferString()
    {
        double total;
        double received;
        String unit;

        if(totalLength >= 1024*1024*1024)
        {
            total=totalLength/(double)(1024*1024*1024);
            unit = "GB";
            received = receivedLength/(double)(1024*1024*1024);
        }
        else if(totalLength >= 1024*1024)
        {
            total=totalLength/(double)(1024*1024);
            unit = "MB";
            received = receivedLength/(double)(1024*1024);
        }
        else if(totalLength >= 1024)
        {
            total=totalLength/(double)1024;
            unit = "KB";
            received = receivedLength/(double)1024;
        }
        else
        {
            total = totalLength;
            unit = "Bytes";
            received = receivedLength;
        }

        return String.format("%.2f",received) + "/" + String.format("%.2f",total) + " " + unit;
    }
}
