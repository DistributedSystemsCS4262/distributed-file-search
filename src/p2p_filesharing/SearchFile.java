/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Kanchana
 */
public class SearchFile {
    
    private final DatagramSocket socket;
    private final Node node;
    
    public SearchFile(Node node, DatagramSocket socket) {
        this.node = node;
        this.socket = socket;
    }
    
    public void searchDiscMsgRecieved(String data){
        StringTokenizer token = new StringTokenizer(data);
        String length = token.nextToken();
        String command = token.nextToken();
        Set<Neighbour> neighbourList=node.getNeighbours();
        String ip = token.nextToken();
        int port = Integer.parseInt(token.nextToken());
        String timestamp = token.nextToken();
        if(!this.node.searchRequestAvailable(data)){
            this.node.addRequestAvailable(data);
            discAckMsg(timestamp,ip,port);
            for(Neighbour neighbour:neighbourList){
                node.sendPacket(data, neighbour.getIp(), neighbour.getPort());
            }
        }        
    }
    
    public void searchMsgRecieved(String data){
        StringTokenizer token = new StringTokenizer(data);
        String length = token.nextToken();
        String command = token.nextToken();
        String ip = token.nextToken();
        int port = Integer.parseInt(token.nextToken());
        token=new StringTokenizer(data,"\"");
        token.nextToken();
        String filename = token.nextToken();
        token = new StringTokenizer(filename);
        HashMap<String, ArrayList<String>> fileDictionary = this.node.getFileDictionary();
        String word = token.nextToken();
        ArrayList<String> result = fileDictionary.get(word);
        while(token.hasMoreTokens()){
            word = token.nextToken();
            result.retainAll(fileDictionary.get(word));
        }
        ArrayList<String> filelist = new ArrayList<>();
        String searchString = data.replace(" ", "#")+"#";
        for(String file:result){
            String fileModified = file.replace(" ", "#")+"#";
            if(fileModified.contains(searchString)){
                filelist.add(file);
            }
        }
        String searchOk ="SEROK "+filelist.size()+" "+node.getIp()+" "+node.getPort()+" 1";
        for(String file:filelist){
            searchOk=searchOk+ " " + file;
        }
        String lengthOfMsg = node.getlength(searchOk);
        searchOk = lengthOfMsg + " " + searchOk;
        node.sendPacket(searchOk, ip, port);
    }
    
    public void discAckMsg(String timestamp,String ip,int port){
        String discAck = "DISCACK " + node.getIp() +" "+ node.getPort()+" "+timestamp;
        String length = this.node.getlength(discAck);
        discAck = length + " " + discAck;
        node.sendPacket(discAck, ip, port);
    }
    
}
