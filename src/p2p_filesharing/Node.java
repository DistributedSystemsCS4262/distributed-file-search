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
    
    private String ip;
    private int port;
    private String name;
    
    private DatagramSocket datagramSocket;
    
    private ArrayList<Neighbour> neighbours ;

    public Node(String ip, int port, String name,DatagramSocket datagramSocket  ) {
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

    public void register(){
        
   
    String register_msg="REG " + ip + " " + port + " " + name;
    register_msg=this.getlength(register_msg)+ " " + register_msg;
     System.out.println(register_msg);
    this.sendPacket(register_msg, "127.0.0.1", 55555);
   
    
    }
    
    
    public String getlength(String msg){
    
    String length="0000";
    int x=msg.length()+5;
    if(x < 10 && x > 0) {
            length = "000" + x;
        }
        if(x >= 10 && x < 100){
            length = "00" + x;

        }
        if(x >= 100 && x < 999){
            length = "0" + x;

        }
        if(x >= 1000){
            length = "" + x;

        }
    
    
    
    
    return length;
    }
    
    
    
    
    
    
    
    
    
    
}
