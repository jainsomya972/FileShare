package App.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoveryClient implements Runnable{

    public static ServerSocket socket;
    private Socket fromClient;
    private String name;
    private static int port;

    public DiscoveryClient(int port, String name){
        try{
            this.name=name;
            this.port = port;
            socket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        //Server is running always. This is done using this while(true) loop
        if(!socket.isClosed()) {
            while (true) {
                //Reading the message from the client
                try {
                    fromClient = socket.accept();
                    InputStream is = fromClient.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String number = br.readLine();
                    System.out.println("Message received from client is " + number);

                    String returnMessage;
                    returnMessage = Main.name + "\n";

                    //Sending the response back to the client.
                    OutputStream os = fromClient.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write(returnMessage);
                    System.out.println("Message sent to the client is " + returnMessage);
                    bw.flush();
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

    /*public static boolean ServerSocketIsClosed(){
        if(socket.isClosed()){
            return true;
        }
        return false;
    }*/

}
