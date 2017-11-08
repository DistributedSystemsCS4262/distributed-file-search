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

        DatagramSocket datagramSocket = new DatagramSocket();
        Node node = new Node("127.0.0.1", datagramSocket.getLocalPort(), "Hashini", datagramSocket);
        Listner listner = new Listner(node, datagramSocket);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            synchronized (System.out) {
                System.out.println("Enter command");
                System.out.println("r : Register with bootstrap server");
                System.out.println("u : Unregister from bootstrap server");
                // System.out.println("j : joinWithNeighbours ");
                System.out.println("l : leave distributed system");
                System.out.println("s : serch file");
                System.out.println("p : print neighbours");
            }

            String in = scanner.nextLine().toLowerCase();

            if (in.equals("r")) {

                node.register();
                new Thread(listner).start();

            } else if (in.equals("u")) {
                node.UnRegister();

            } else if (in.equals("j")) {

            } else if (in.equals("l")) {
                node.leave();

            } else if (in.equals("s")) {
                 SearchFile ser = new SearchFile(node,datagramSocket);
                 System.out.println("Enter file name:");
                 String filename = scanner.nextLine();
                 filename= "\""+filename+"\"";
                 for(Neighbour s:node.getNeighbours()){
                    ser.initiateSearchMsg(String.valueOf(System.currentTimeMillis()), s.getIp(), s.getPort(), filename);
                 }               
            } else if (in.equals("p")) {
                //print neighbours
                node.print_neighbours();
            } else if (in.equals("e")) {
                //print neighbours
                System.exit(0);
            } else {

            }
        }
    }

}
