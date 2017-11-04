package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.Constants;

import java.io.IOException;
import java.net.*;

public class Node extends Thread {
    private DatagramSocket socket;
    private MessageParser messageParser;

    public Node(){
        try {
            socket = new DatagramSocket();
            Constants.PORT = socket.getLocalPort();
            messageParser = new MessageParser(this);
            this.start();
//            System.out.println(socket.getLocalAddress().toString());
        } catch (SocketException e) {
            System.out.println("Unable to start socket!");
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                socket.receive(packet);

                if (packet.getLength() > 0) {
                    System.out.println("data processing");
                    messageParser.parseMessage(packet);

                }else{
                    System.out.println("Packet received, but no data!");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void send(String message,String ip,int port){
        System.out.println("sending msg:" + message);
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
    }
}
