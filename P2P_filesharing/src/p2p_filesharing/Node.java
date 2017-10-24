/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Scorpius
 */
public class Node {
    
    private String port;
    private int ip;
    private String name;
    
    private DatagramSocket datagramSocket;
    
    private ArrayList<Neighbour> neighbours ;

    public Node(String port, int ip, String name,DatagramSocket datagramSocket  ) {
        this.port = port;
        this.ip = ip;
        this.name = name;
        this.neighbours = new ArrayList<Neighbour>();
        
        this.datagramSocket = datagramSocket;
    }
    //implement register leave 
    public void sendPacket(String message, String ip, int port)  {
       

        byte[] messageContentBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(messageContentBytes, messageContentBytes.length);

        try {
            packet.setAddress(InetAddress.getByName(ip));
            packet.setPort(port);

            datagramSocket.send(packet);
        } catch (UnknownHostException ex) {
            
        } catch (IOException ex) {
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
}
