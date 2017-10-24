/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

/**
 *
 * @author Scorpius
 */
public class Listner implements Runnable {

    private final DatagramSocket socket;
    private final Node node;

    public Listner(Node node, DatagramSocket socket) {
        this.node = node;
        this.socket = socket;
    }

    private void processData(DatagramPacket packet) {
        byte[] dataRaw = packet.getData();
        String data = new String(dataRaw, 0, packet.getLength());
        // handle data , implement parser
        StringTokenizer token = new StringTokenizer(data);
        String lenght = token.nextToken();
        String command = token.nextToken();

        switch (command) {
            case "REGOK":
                this.register_ok(token);
                break;
            case "UNROK":
                 this.unregister_ok();
                 break;
            case "JOINOK":
                this.join_ok();
                break;
                
            case "LEAVEOK":
                this.leave_ok();
                break;
                
            case "SEROK":
                this.search_ok();
                break;
            case "ERROR":
                this.error();
                break;
            default:
                break;

        }

    }

    private void register_ok(StringTokenizer token) {
        System.out.println(token);
    }
    
     private void unregister_ok() {

    }
      private void join_ok() {

    }

       private void leave_ok() {

    }
        private void search_ok() {

    }
      
      private void error() {

    }
      
      
    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                socket.receive(packet);
                System.out.println("data processing");

                if (packet.getLength() > 0) {
                    System.out.println("data processing");
                    this.processData(packet);
                    

                }

            } catch (IOException ignored) {
            }
        }
    }

}
