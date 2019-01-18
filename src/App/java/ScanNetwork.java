package App.java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class ScanNetwork implements Runnable {

    public List<String> foundiplist;
    private int port;
    DiscoveryDialogController discoveryDialogController;
    //CyclicBarrier barrier;

    @Override
    public void run() {
        foundiplist=checkip();
        discoveryDialogController.OnScanCompletion(foundiplist);
        //System.out.println("list being returned : " + foundiplist.toArray()[0]);
    }

    public ScanNetwork(int port, DiscoveryDialogController discoveryDialogController)
    {
        foundiplist = new ArrayList<>();
        this.port = port;
        this.discoveryDialogController = discoveryDialogController;
        //barrier = new CyclicBarrier(1);
    }

    public List<String> getListOfIPsFromNIs() throws SocketException {
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

    public List<String> checkip(){
        List<String> list = new ArrayList<>();
        try {
            for(String ip : getListOfIPsFromNIs()) {
                String lastOctet = ip.split("\\.")[ip.split("\\.").length - 1];
                String remainingip = ip.replace("." + lastOctet, "") + ".";

                for (int i = 1; i < 255; i++) {
                    String i_string = Integer.toString(i);
                    if (!i_string.equals(lastOctet)) {
                        String itrip = remainingip + i_string;
                        System.out.print(".");
                        Socket socket = new Socket();
                        try {
                            socket.connect(new InetSocketAddress(itrip, port), 8);
                            socket.close();
                            System.out.println("\nip : " + itrip);
                            list.add(itrip);
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return list;
    }
}
