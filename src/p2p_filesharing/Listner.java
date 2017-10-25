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
        System.out.println("Receiving msg "+data);
        StringTokenizer token = new StringTokenizer(data);
        String lenght = token.nextToken();
        String command = token.nextToken();
        SearchFile ser = new SearchFile(this.node,this.socket);
        switch (command) {
            case "REGOK":
                this.register_ok(token);
                break;
            case "UNROK":
                this.unregister_ok(token);
                break;
            case "JOIN":
                this.join(token);
                break;
            case "JOINOK":
                this.join_ok(token, packet);
                break;
            case "LEAVE":
                this.leave(token);
                break;
            case "LEAVEOK":
                this.leave_ok(token, packet);
                break;
            case "DISC":
                ser.searchDiscMsgRecieved(data);
                break;
            case "DISCACK":
                
                ser.search(data);
                break;
            case "SER":
                ser.searchMsgRecieved(data);
                //this.search_ok();
                break;
            case "SEROK":
                System.out.println(data);
                this.search_ok(token);
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
        ArrayList<Neighbour> new_neighbours = new ArrayList<>();
        // add neighbours
        if (no_of_neighbours > 0 && no_of_neighbours < 9996) {
            while (token.hasMoreTokens()) {
                String ip = token.nextToken();
                String Nport = token.nextToken();
                new_neighbours.add(new Neighbour(ip, Integer.parseInt(Nport)));

                if (new_neighbours.size() == 2) {
                    break;
                }
            }
            node.join(new_neighbours);
        } else {

        }

    }

    private void unregister_ok(StringTokenizer token) {
        int value = Integer.parseInt(token.nextToken());
        if (value == 0) {

        } else {

        }
    }

    private void join(StringTokenizer token) {
        String host = token.nextToken();
        int port = Integer.parseInt(token.nextToken());
        int value = 0;
        try {
            node.addNeighbour(new Neighbour(host, port));
        } catch (Exception ex) {
            value = 9999;
        }
        String ack = "JOINOK " + value;
        ack = node.getlength(ack) + " " + ack;

        node.sendPacket(ack, host, port);
    }

    private void join_ok(StringTokenizer token, DatagramPacket packet) {
        int value = Integer.parseInt(token.nextToken());
        if (value == 0) {
            node.addNeighbour(new Neighbour(packet.getAddress().getHostAddress(), packet.getPort()));
        } else {

        }
    }

    private void leave(StringTokenizer token) {
        String host = token.nextToken();
        int port = Integer.parseInt(token.nextToken());
        int value = 0;
        try {
            node.removeNeighbour(new Neighbour(host, port));
        } catch (Exception ex) {
            value = 9999;
        }

        String ack = "LEAVEOK " + value;
        ack = node.getlength(ack) + " " + ack;
        node.sendPacket(ack, host, port);

    }

    private void leave_ok(StringTokenizer token, DatagramPacket packet) {
        int value = Integer.parseInt(token.nextToken());
        if (value == 0) {
            node.removeNeighbour(new Neighbour(packet.getAddress().getHostAddress(), packet.getPort()));
        } else {

        }
    }

    private void search_ok(StringTokenizer token) {

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

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
