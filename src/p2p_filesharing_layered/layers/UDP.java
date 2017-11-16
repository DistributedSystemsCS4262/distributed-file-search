package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.Constants;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDP extends Thread {

    private DatagramSocket socket;
    private MessageParser messageParser;
    private WebService webService;

    public UDP() {
        try {
            socket = new DatagramSocket();
            Constants.PORT = socket.getLocalPort();
            messageParser = new MessageParser(this);
            webService = new WebService(messageParser);
            webService.start();
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

                } else {
                    System.out.println("Packet received, but no data!");
                }
                //Thread.sleep(1000);
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
//            catch (InterruptedException ex) {
//                Logger.getLogger(UDP.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    public void send(String message, String ip, int port) {

        if (Constants.UDP_MODE==2 && !message.contains("REG") && !message.contains("UNROK")) {
            webService.sendPacket(message, ip, port);
            System.out.println("web S send"+message + " : "+ip+" , "+port);
            return;
        }

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
