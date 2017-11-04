package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;

public class Message {
    private String action;
    private String ip;
    private int port;

    public Message(String action,String ip, int port){
        this.action = action;
        this.ip = ip;
        this.port = port;
    }

    public String packetMessage(){
        String join_msg = action +" " + Constants.IP_ADDRESS + " " + Constants.PORT;
        join_msg = Utility.getMessageLength(join_msg) + " " + join_msg;
        return join_msg;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAction() {
        return action;
    }
}
