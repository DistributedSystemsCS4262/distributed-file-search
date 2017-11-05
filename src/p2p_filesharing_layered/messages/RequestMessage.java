package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;

import java.util.StringTokenizer;

public class RequestMessage {
    private String action;
    private String ip;
    private int port;

    public RequestMessage(){}

    public RequestMessage(String action){
        this.action = action;
    }

    public RequestMessage(String action, StringTokenizer stringTokenizer) {
        this.action = action;
        this.ip = stringTokenizer.nextToken();
        this.port = Integer.parseInt(stringTokenizer.nextToken());
    }

    public RequestMessage(String action, String ip, int port){
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

    public void setAction(String action) {
        this.action = action;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
