/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
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
        
        int no_of_neighbours = Integer.valueOf(token.nextToken());
        // add neighbours
        if (no_of_neighbours == 0) {
            System.out.println("No neighbours");
            
        } else if (no_of_neighbours == 1) {
            ArrayList<Neighbour> new_neighbours = new ArrayList<>();
            String new_ip = token.nextToken();
            int new_port = Integer.valueOf(token.nextToken());
            Neighbour neighbour = new Neighbour(new_ip, new_port);
            new_neighbours.add(neighbour);
            node.setNeighbours(new_neighbours);
        }else if (no_of_neighbours == 2) {
            ArrayList<Neighbour> new_neighbours = new ArrayList<>();
            String new_ip = token.nextToken();
            int new_port = Integer.valueOf(token.nextToken());
            Neighbour neighbour = new Neighbour(new_ip, new_port);
            new_neighbours.add(neighbour);
            
            new_ip = token.nextToken();
            new_port = Integer.valueOf(token.nextToken());
            neighbour = new Neighbour(new_ip, new_port);
            new_neighbours.add(neighbour);
            
            
            node.setNeighbours(new_neighbours);
            System.out.println("added 2 neighbours");
        }
        
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
                
                if (packet.getLength() > 0) {
                    System.out.println("data processing");
                    this.processData(packet);
                    
                }
                
            } catch (IOException ignored) {
            }
        }
    }
    
}
