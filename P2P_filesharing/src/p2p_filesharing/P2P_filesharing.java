/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import jdk.nashorn.tools.ShellFunctions.*;
import static jdk.nashorn.tools.ShellFunctions.input;

/**
 *
 * @author Scorpius
 */
public class P2P_filesharing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
                
             /*   DatagramSocket datagramSocket = new DatagramSocket();
                Node node =new Node("127.0.0.1", datagramSocket.getLocalPort(), "Hashini",datagramSocket);
                Listner listner = new Listner(node, datagramSocket);
                
                Scanner scanner = new Scanner(System. in);
                
              while(true){
              
              System.out.println("Enter command");
              String in = scanner.nextLine();

              if(in.equals("join")){
              
              }else{
              
              }
              }*/
            String msg= "0012 REG test";
            Message msgCl = new Message();
            System.out.println(msgCl.getMessageClass(msg));
            switch(msgCl.getMessageClass(msg)){
                case REG:System.out.println(msgCl.getMessageClass(msg));break;
                default:System.out.println("Test");
            }
    }
    
}
