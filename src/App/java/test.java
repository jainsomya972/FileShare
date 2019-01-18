package App.java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class test {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        List<String> ips = getListOfIPsFromNIs();
        for(String ip : ips)
        {
            checkip(ip, 9090);
        }
    }

    public static void checkPorts() {
        for (int port = 1; port <= 65535; port++)
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", port), 300);
                socket.close();
                System.out.println("Port " + port + " is open");
            } catch (SocketTimeoutException ex) {
            } catch (IOException ex) {
            }
    }
    public static List<String> getListOfIPsFromNIs() throws SocketException {
        List<String> addrList = new ArrayList<>();
        Enumeration<NetworkInterface> enumNI = NetworkInterface.getNetworkInterfaces();
        while ( enumNI.hasMoreElements() ){
            NetworkInterface ifc = enumNI.nextElement();
            if( ifc.isUp() ){
                Enumeration<InetAddress> enumAdds = ifc.getInetAddresses();
                while ( enumAdds.hasMoreElements() ){
                    InetAddress addr = enumAdds.nextElement();
                    String ip = addr.getHostAddress();
                    if(!ip.contains(":") && !ip.equals("127.0.0.1")) {
                        addrList.add(ip);
                        //System.out.println("IP : " + ip);  //<---print ip
                    }
                }
            }
        }
        return addrList;
    }
    public static void checkip(String ip, int port)
    {
        String lastOctet = ip.split("\\.")[ip.split("\\.").length-1];
        String remainingip = ip.replace("." + lastOctet,"") + ".";

        for(int i=1;i<255;i++)
        {
            String i_string = Integer.toString(i);
            if(!i_string.equals(lastOctet))
            {
                String itrip = remainingip + i_string;
                System.out.print(".");
                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(itrip, port), 4);
                    socket.close();
                    System.out.println("\nip : " + itrip);
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
