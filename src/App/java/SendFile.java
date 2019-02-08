package App.java;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFile implements Runnable{

    private int port = 6700;
    private List<File> fileToSend;
    private String selectedIP;


    private ProgressBar progressBar;
    private Label progressLabel;
    private Label percentLabel;
    private JFXListView<String> listview_files;

    private long totalLength = 0;
    private long sendLength = 0;

    public SendFile(String selectedIP,List<File> file_to_send, ProgressBar progressBar, Label percentLabel,Label progressLabel, JFXListView<String> listview_files){
        this.selectedIP = selectedIP;
        fileToSend = file_to_send;
        this.progressLabel = progressLabel;
        this.progressBar = progressBar;
        this.percentLabel = percentLabel;
        this.listview_files = listview_files;
    }

    private void SendFileMethod(String receiver, List<File> file_to_send){

        Socket sendToServer=null;
        ArrayList<String> relativePaths = getPaths(file_to_send);
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    listview_files.getItems().clear();
                }
            });
            sendToServer = new Socket(receiver,port);
            //Send the message to the server
            OutputStream os = sendToServer.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            DataOutputStream out = new DataOutputStream(sendToServer.getOutputStream());
            String sendMessage = "filetransfer\n"+file_to_send.size()+"\n";

            //String sendMessage = f.getName() + "\n"+ f.length() + "\n";
            bw.write(sendMessage);
            bw.flush();
            System.out.println("message: "+sendMessage);
            for(String p:relativePaths){
                bw.write(p+"\n");
                bw.flush();
                System.out.println(p);
            }

            //PrintStream out = new PrintStream(sendToServer.getOutputStream(), true);
            int filecount = 0;
            for(File f: file_to_send){
                final int finalFileCount = filecount;
                final String finalFileName = f.getName();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listview_files.getItems().add(finalFileName);
                        listview_files.scrollTo(finalFileCount);
                    }
                });
                FileInputStream requestedfile = new FileInputStream(f.getPath());
                System.out.println("file path: "+f.getPath());

                int count;
                byte[] buffer = new byte[65536];
                while ((count = requestedfile.read(buffer)) > 0)
                {
                    sendLength+=count;
                    out.write(buffer, 0, count);
                    //System.out.println(buffer);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //label_sender_name.setText("Receiving from " + senderName + " ...");
                            //label_data_size.setText(GetDataTransferString());
                            progressLabel.setText(GetDataTransferString());
                            progressBar.setProgress(sendLength/(double)totalLength);
                            percentLabel.setText(String.format("%.2f",(sendLength/(double)totalLength)*100) + " %");
                        }
                    });
                }

                System.out.println("File transfer completed!! voila! :)");
                requestedfile.close();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String remove = listview_files.getItems().remove(finalFileCount);
                        listview_files.getItems().add(remove  + "\ndone");
                    }
                });
                filecount++;

            }

            out.close();

            sendToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getPaths(List<File> f){
        ArrayList<String> paths = new ArrayList<>();
        totalLength =0;
        for(File fi: f){
            try{
                paths.add(fi.getName()+"\n"+fi.length());
                totalLength += fi.length();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return paths;
    }

    public void run(){
        SendFileMethod(selectedIP,fileToSend);
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
            received = sendLength/(double)(1024*1024*1024);
        }
        else if(totalLength >= 1024*1024)
        {
            total=totalLength/(double)(1024*1024);
            unit = "MB";
            received = sendLength/(double)(1024*1024);
        }
        else if(totalLength >= 1024)
        {
            total=totalLength/(double)1024;
            unit = "KB";
            received = sendLength/(double)1024;
        }
        else
        {
            total = totalLength;
            unit = "Bytes";
            received = sendLength;
        }

        return String.format("%.2f",received) + "/" + String.format("%.2f",total) + " " + unit;
    }
}
