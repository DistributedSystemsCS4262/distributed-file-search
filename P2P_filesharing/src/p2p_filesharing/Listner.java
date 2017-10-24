/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                socket.receive(packet);

                if (packet.getLength() > 0) {
                    processData(packet);
                    
                }

            } catch (IOException ignored) {
            }
        }
    }

}
