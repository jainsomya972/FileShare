package App.java;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFolder implements Runnable{

    private File folder = MainController.folder;
    private int port = 6700;
    private List<File> fileToSend;
    private String selectedIP;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Label percentLabel;

    private long totalLength = 0;
    private long sendLength = 0;

    public SendFolder(String selectedIP, List<File> file_to_send, ProgressBar progressBar, Label percentLabel,Label progressLabel){
        this.selectedIP = selectedIP;
        fileToSend = file_to_send;
        this.progressLabel = progressLabel;
        this.progressBar = progressBar;
        this.percentLabel = percentLabel;
    }

    private void SendFolderMethod(String receiver, List<File> file_to_send){
        Socket sendToServer=null;
        ArrayList<String> relativePaths = getRelativePaths(file_to_send);
        try {
            sendToServer = new Socket(receiver,port);
            //Send the message to the server
            OutputStream os = sendToServer.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            DataOutputStream out = new DataOutputStream(sendToServer.getOutputStream());
            String sendMessage = "foldertransfer\n"+file_to_send.size()+"\n";

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
            for(File f: file_to_send){

                /*sendMessage = String.valueOf(f.length()) + "\n";
                bw.write(sendMessage);
                bw.flush();*/

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
                            percentLabel.setText((sendLength/(double)totalLength)*100 + " %");
                        }
                    });
                }
                out.flush();
                System.out.println("File transfer completed!! voila! :)");
                requestedfile.close();

            }
            System.out.println("Folder transfer complete");
            out.close();
            sendToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> getRelativePaths(List<File> f){
        ArrayList<String> paths = new ArrayList<>();
        String parentPath = folder.getParent();
        totalLength=0;
        for(File fi: f){
            try{
                paths.add(fi.getCanonicalPath().replace(parentPath,"")+"\n"+fi.length());
                totalLength+=fi.length();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return paths;
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



    public void run(){
        SendFolderMethod(selectedIP,fileToSend);
    }
}

