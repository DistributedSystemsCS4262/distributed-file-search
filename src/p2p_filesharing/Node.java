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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    private Set<Neighbour> neighbours;
    private HashSet<String> requests;
    private HashSet<String> fileList;
    private HashMap<String, ArrayList<String>> fileDictionary;

    public Node(String ip, int port, String name, DatagramSocket datagramSocket) {
        this.port = port;
        this.ip = ip;
        this.name = name;
        this.neighbours = ConcurrentHashMap.newKeySet();
        this.requests = new HashSet<String>();
        this.datagramSocket = datagramSocket;
    }

    //implement register leave 
    public synchronized void sendPacket(String message, String ip, int port) {
        System.out.println("sending msg:" + message);
        byte[] messageContentBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(messageContentBytes, messageContentBytes.length);

        try {
            packet.setAddress(InetAddress.getByName(ip));
            packet.setPort(port);

            getDatagramSocket().send(packet);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void register() {

        String register_msg = "REG " + getIp() + " " + getPort() + " " + getName();
        register_msg = this.getlength(register_msg) + " " + register_msg;
        System.out.println(register_msg);
        this.sendPacket(register_msg, "127.0.0.1", 55555);

    }

    public void UnRegister() {

        String register_msg = "UNREG " + getIp() + " " + getPort() + " " + getName();
        register_msg = this.getlength(register_msg) + " " + register_msg;
        System.out.println(register_msg);
        this.sendPacket(register_msg, "127.0.0.1", 55555);

    }

    public void join(ArrayList<Neighbour> new_neighbours) {
        for (Neighbour neighbour : new_neighbours) {
            String join_msg = "JOIN " + getIp() + " " + getPort();
            join_msg = this.getlength(join_msg) + " " + join_msg;

            System.out.println(join_msg);
            this.sendPacket(join_msg, neighbour.getIp(), neighbour.getPort());
        }

    }

    public void leave() {
        for (Neighbour neighbour : neighbours) {
            String leave_msg = "LEAVE " + getIp() + " " + getPort();
            leave_msg = this.getlength(leave_msg) + " " + leave_msg;

            System.out.println(leave_msg);
            this.sendPacket(leave_msg, neighbour.getIp(), neighbour.getPort());
        }
    }

    public void print_neighbours() {
        // print neighbours
        synchronized (System.out) {
            System.out.println("set of neighbours");
            for (Neighbour neighbour : neighbours) {
                System.out.println(String.format("\t\tIp %s : Port %d", neighbour.getIp(), neighbour.getPort()));

            }
        }

    }

    public String getlength(String msg) {

        String length = "0000";
        int x = msg.length() + 5;
        if (x < 10 && x > 0) {
            length = "000" + x;
        }
        if (x >= 10 && x < 100) {
            length = "00" + x;

        }
        if (x >= 100 && x < 999) {
            length = "0" + x;

        }
        if (x >= 1000) {
            length = "" + x;

        }

        return length;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the datagramSocket
     */
    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    /**
     * @param datagramSocket the datagramSocket to set
     */
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

//    public Set<Neighbour> getNeighbours() {
//        return neighbours;
//    }
    public synchronized void addNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }

    public synchronized void removeNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }
    public HashMap<String, ArrayList<String>> getFileDictionary(){
        return this.fileDictionary;
    }
    public boolean searchRequestAvailable(String data){
        if(!this.requests.isEmpty()){
            if(this.requests.contains(data)){
                return true;
            }
            return false;
        }
        return false;
    }
    
    public Set<Neighbour> getNeighbours() {
        return neighbours;
    }
    
    public void addRequestAvailable(String data){
        this.requests.add(data);
    }
    
    public void addFile(String fileName){
        this.fileList.add(fileName);
    }
    
    public ArrayList<String> findFiles(String fileName){
        ArrayList<String> result=new ArrayList<String>();
        
        return result;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
// */
//package p2p_filesharing;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Scorpius
// */
//public class Node {
//    
//    private String ip;
//    private int port;
//    private String name;
//    
//    private DatagramSocket datagramSocket;
//    
//    private ArrayList<Neighbour> neighbours ;
//    private HashSet<String> requests;
//    private HashSet<String> fileList;
//    private HashMap<String, ArrayList<String>> fileDictionary;
//        
//    public Node(String ip, int port, String name,DatagramSocket datagramSocket  ) {
//        this.port = port;
//        this.ip = ip;
//        this.name = name;
//        this.neighbours = new ArrayList<Neighbour>();
//        this.requests = new HashSet<String>();
//        this.datagramSocket = datagramSocket;
//    }
//    //implement register leave 
//    public void sendPacket(String message, String ip, int port)  {
//       
//
//        byte[] messageContentBytes = message.getBytes();
//        DatagramPacket packet = new DatagramPacket(messageContentBytes, messageContentBytes.length);
//
//        try {
//            packet.setAddress(InetAddress.getByName(ip));
//            packet.setPort(port);
//
//            getDatagramSocket().send(packet);
//        } catch (UnknownHostException ex) {
//            
//        } catch (IOException ex) {
//        }
//    }
//
//    public void register(){
//        
//   
//    String register_msg="REG " + getIp() + " " + getPort() + " " + getName();
//    register_msg=this.getlength(register_msg)+ " " + register_msg;
//     System.out.println(register_msg);
//    this.sendPacket(register_msg, "127.0.0.1", 55555);
//   
//    
//    }
//     void print_neighbours() {
//        // print neighbours
//         
//         
//         
//    }
//    
//    public String getlength(String msg){
//    
//    String length="0000";
//    int x=msg.length()+5;
//    if(x < 10 && x > 0) {
//            length = "000" + x;
//        }
//        if(x >= 10 && x < 100){
//            length = "00" + x;
//
//        }
//        if(x >= 100 && x < 999){
//            length = "0" + x;
//
//        }
//        if(x >= 1000){
//            length = "" + x;
//
//        }
//    
//    
//    
//    
//    return length;
//    }
//
//    /**
//     * @return the ip
//     */
//    public String getIp() {
//        return ip;
//    }
//
//    /**
//     * @param ip the ip to set
//     */
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    /**
//     * @return the port
//     */
//    public int getPort() {
//        return port;
//    }
//
//    /**
//     * @param port the port to set
//     */
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    /**
//     * @return the name
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * @param name the name to set
//     */
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    /**
//     * @return the datagramSocket
//     */
//    public DatagramSocket getDatagramSocket() {
//        return datagramSocket;
//    }
//
//    /**
//     * @param datagramSocket the datagramSocket to set
//     */
//    public void setDatagramSocket(DatagramSocket datagramSocket) {
//        this.datagramSocket = datagramSocket;
//    }
//
//    /**
//     * @return the neighbours
//     */
//    public ArrayList<Neighbour> getNeighbours() {
//        return neighbours;
//    }
//
//    /**
//     * @param neighbours the neighbours to set
//     */
//    public void setNeighbours(ArrayList<Neighbour> neighbours) {
//        this.neighbours = neighbours;
//    }
//    
//    
//    
//    
//    
//    
//    
//    
//}
