package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;
import p2p_filesharing_layered.domain.Neighbour;

import java.util.Set;
import java.util.StringTokenizer;

public class DiscoverMessage extends RequestMessage {
    private String fileName;
    private String timeStamp; 
    private int hopCount;

    public DiscoverMessage(String receivedData){
        StringTokenizer token2 = new StringTokenizer(receivedData,"\"");
        StringTokenizer token = new StringTokenizer(token2.nextToken());
        String length = token.nextToken();
        setAction( token.nextToken());
        this.fileName = token2.nextToken();
        token = new StringTokenizer(token2.nextToken());
        setIp(token.nextToken());
        setPort(Integer.parseInt(token.nextToken()));
        this.timeStamp = token.nextToken();
        this.hopCount = Integer.parseInt(token.nextToken());
    }

    public DiscoverMessage(String action, String ip, int port, String fileName, String timeStamp,int hopCount) {
        super(action, ip, port);
        this.fileName = fileName;
        this.timeStamp = timeStamp;
        this.hopCount=hopCount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setHopCount(int hopCount){
        this.hopCount = hopCount;
    }
    
    public int getHopCount(){
        return hopCount;
    }
    
    @Override
    public String packetMessage() {
        String join_msg = getAction() + " \"" + fileName + "\" " + Constants.IP_ADDRESS + " " + Constants.PORT + " " + this.timeStamp+ " "+this.hopCount;
        join_msg = Utility.getMessageLength(join_msg) + " " + join_msg;
        return join_msg;
    }
}
