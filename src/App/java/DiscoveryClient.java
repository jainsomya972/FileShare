package App.java;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DiscoveryClient implements Runnable{

    public static ServerSocket socket;
    private Socket fromClient;
    private String name;
    private static int port;
    private File folder;

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
            String name = inputMessage.split(" ")[1];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String returnMessage = "unknown";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            name + " is sending some Files, do you want to receive it?",
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
            int fileCount = Integer.parseInt(br.readLine());
            System.out.println("Files Count : " + fileCount);

            for (int count = 0; count < fileCount; count++) {
                String fileName = dis.readUTF();
                int length = Integer.parseInt(fileName.split("\n")[1]);
                fileName = fileName.split("\n")[0];
                System.out.println("File Name : " + fileName);
                System.out.println("Length : " + length);
                System.out.println("File Data : ");
                File file = new File(folder.getPath() +"/"+ fileName);

                //file.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
               /* byte[] item;
                item = new byte[length];
                byte temp;
                for (int i=0; i<length ; i++) {
                    try {
                        temp = dis.readByte();
                        if(temp!='\0')
                        item[i] = temp;
                        System.out.println(i);
                    }catch(EOFException eof){
                        System.out.println("fuck off EOF");
                    }
                }*/

                int received = 0;
                int c;
                byte[] buffer = new byte[65536];
                while (received < length && (c = dis.read(buffer, 0, Math.min(buffer.length, length - received))) > 0) {
                    fos.write(buffer, 0, c);
                    fos.flush();
                    received += c;
                }

                //fos.write(item);
                fos.close();
                System.out.println("\nFile received successfully!!! voila !! :)");
                //br.readLine();
            }
        } else if (inputMessage.equals("foldertransfer")) {
            int fileCount = Integer.parseInt(br.readLine());
            System.out.println("Files Count : " + fileCount);
            ArrayList<String> fileNames = new ArrayList<>();
            ArrayList<Integer> fileLengths = new ArrayList<>();
            for (int count = 0; count < fileCount; count++) {
                fileNames.add(br.readLine());
                fileLengths.add(Integer.parseInt(br.readLine()));
                System.out.println(count + "  " + fileNames.get(count)+ "  "+fileLengths.get(count));
            }

            for (int count = 0; count < fileCount; count++) {

                //int length = Integer.parseInt(br.readLine());
                //fileName = fileName.split("\n")[0];
                //System.out.println("File Name : " + fileName);
                //String fileName = dis.readUTF();

                //fileName = fileName.split("\n")[0];
                int length = fileLengths.get(count);
                System.out.println("Length : " + length);
                System.out.println("Store path : " + folder.getPath()+fileNames.get(count));
                File file = new File(folder.getPath()+fileNames.get(count));
                String[] paths = file.getPath().split("/");
                String path = paths[paths.length-1];
                path = (folder.getPath()+fileNames.get(count)).replace(path,"");

                new File(path).mkdirs();
                //file.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);

                int received = 0;
                int c;
                byte[] buffer = new byte[65536];
                while (received < length && (c = dis.read(buffer, 0, Math.min(buffer.length, length - received))) > 0) {
                    fos.write(buffer, 0, c);
                    fos.flush();
                    received += c;
                }

                fos.close();
                System.out.println("\nFile received successfully!!! voila !! :)");

            }
            System.out.print("Folder received successfully");
        }


    }
}
