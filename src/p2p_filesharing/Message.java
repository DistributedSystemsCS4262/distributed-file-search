/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Scorpius
 */

public class Message {
    private String message;
    private MessageClass msgCls;
    private int msgLength;
    private Map<String, String> parameters;
    public Message(){
        
    }
    
    public Message(String msg){
        
    }
    
    public Message(MessageClass type, String ip, int port, String name){
        
    }
    
    public static MessageClass getMessageClass(String message){
        StringTokenizer strToken=new StringTokenizer(message," ");
        strToken.nextToken();
        MessageClass msgCls= MessageClass.valueOf(strToken.nextToken());// = ;
        return msgCls;
    }
    
    public static int getMessageLength(String message){
        int length;
        StringTokenizer strToken=new StringTokenizer(message," ");
        length= Integer.parseInt(strToken.nextToken());
        return length;
    }
    
    
}
