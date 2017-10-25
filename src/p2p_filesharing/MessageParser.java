/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

/**
 *
 * @author Kanchana
 */
public class MessageParser {
    Message msg;
    public void parseMsgString(String message){
        MessageClass msgCls = Message.getMessageClass(message);
        int msgLength = Message.getMessageLength(message);
        Message msg;
        if(message.length()==msgLength){
            switch(msgCls){
                case REGOK:break;
                case UNROK:break;
                case JOIN:break;
                case JOINOK:break;
                case LEAVEOK:break;
                case SER:break;
                case SEROK:break;
                case DISC:break;
                case DISCACK:break;
            }
        }
        
        //MessageClass msgCls = message.getMessageClass(message);
    }
}
