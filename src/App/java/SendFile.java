package App.java;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SendFile implements Runnable{

    private int port = 6700;
    private List<File> fileToSend;
    private String selectedIP;

    private void SendFileMethod(String receiver, List<File> file_to_send){
        Socket sendToServer=null;
        try {
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

            //PrintStream out = new PrintStream(sendToServer.getOutputStream(), true);
            for(File f: file_to_send){

                sendMessage = f.getName() + "\n"+ f.length() + "\n";
                out.writeUTF(sendMessage);
                //bw.flush();

                FileInputStream requestedfile = new FileInputStream(f.getPath());
                System.out.println("file path: "+f.getPath());

                int count;
                byte[] buffer = new byte[65536];
                while ((count = requestedfile.read(buffer)) > 0)
                {
                    out.write(buffer, 0, count);
                    //System.out.println(buffer);

                }

                System.out.println("File transfer completed!! voila! :)");
                requestedfile.close();

            }

            out.close();
            sendToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public SendFile(String selectedIP,List<File> file_to_send){
        this.selectedIP = selectedIP;
        fileToSend = file_to_send;
    }

    public void run(){
        SendFileMethod(selectedIP,fileToSend);
    }
}
