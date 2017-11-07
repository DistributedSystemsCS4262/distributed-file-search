/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing_layered.messages;

import java.util.ArrayList;
import java.util.StringTokenizer;
import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;
import p2p_filesharing_layered.domain.Neighbour;

/**
 *
 * @author Scorpius
 */
public class SuccessorOkMessage extends RequestMessage{

    
    
    private ArrayList<Neighbour> successors;
    private int number;
    
    
    public SuccessorOkMessage(String action, RequestMessage requestMessage,ArrayList<Neighbour> successors) {
        super(action,requestMessage.getIp(),requestMessage.getPort());
        this.successors= successors;
        this.number = successors.size();
    }
    
    public String packetMessage(){
        String ack = getAction()+" ";
        String members = "";
        
        for (Neighbour neighbour:successors) {
            members += neighbour.getIp() + " " + neighbour.getPort() + " ";
        }
        members = members.trim();

        ack += number + " " + members;
        ack = ack.trim();
        
        ack = Utility.getMessageLength(ack) + " " + ack;
        
        return ack;
    }
   
    
    
}
