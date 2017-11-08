package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.Constants;

import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NodeByRMI implements Runnable,RMIInterface,Node {
    private MessageParser messageParser;
    DatagramSocket socket ;

    public NodeByRMI(){
        try {
            socket = new DatagramSocket();
            Constants.PORT = socket.getLocalPort();
            messageParser = new MessageParser(this);
            new Thread(this).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                            socket.receive(packet);

                            if (packet.getLength() > 0) {
                                System.out.println("data processing");
                                byte[] dataRaw = packet.getData();
                                String data = new String(dataRaw, 0, packet.getLength());
                                messageParser.parseMessage(data,packet.getAddress().getHostAddress(),packet.getPort());
                            }else{
                                System.out.println("Packet received, but no data!");
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(String message, String ip, int port) {
        System.out.println("sending msg:" + message);
        if(ip.equals(Constants.BOOTSTRAP_SERVER_IP) && port == Constants.BOOTSTRAP_SERVER_PORT){
            byte[] messageContentBytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(messageContentBytes, messageContentBytes.length);
            try {
                packet.setAddress(InetAddress.getByName(ip));
                packet.setPort(port);

                socket.send(packet);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            RMIInterface rmiInterface = (RMIInterface) registry.lookup(ip+port);
            rmiInterface.getMessage(message,Constants.IP_ADDRESS,Constants.PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMessage(String data,String ip, int port) {
        messageParser.parseMessage(data,ip,port);
    }


    @Override
    public void run() {
        Registry reg = null;
        try {
            reg = LocateRegistry.createRegistry(Constants.PORT);
        } catch (Exception e) {
            System.out.println("ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        System.out.println("Waiting...");
        try {
            reg.rebind(Constants.IP_ADDRESS+Constants.PORT, (RMIInterface) UnicastRemoteObject.exportObject(this, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
    }
}
