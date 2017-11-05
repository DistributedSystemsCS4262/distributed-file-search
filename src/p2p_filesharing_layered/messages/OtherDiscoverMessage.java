package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.domain.Neighbour;

import java.util.Set;
import java.util.StringTokenizer;

public class OtherDiscoverMessage  extends RequestMessage {
    private String fileName;
    private String timeStamp;
    private String packetMessageData;

    public OtherDiscoverMessage(String receivedData) {
        this.packetMessageData = receivedData;
        StringTokenizer token2 = new StringTokenizer(receivedData, "\"");
        StringTokenizer token = new StringTokenizer(token2.nextToken());
        String length = token.nextToken();
        setAction(token.nextToken());
        this.fileName = token2.nextToken();
        token = new StringTokenizer(token2.nextToken());
        setIp(token.nextToken());
        setPort(Integer.parseInt(token.nextToken()));
        this.timeStamp = token.nextToken();
    }

    public OtherDiscoverMessage(OtherDiscoverMessage o, String ip, int port){
        super(o.getAction(),ip,port);
        this.fileName = o.fileName;
        this.timeStamp = o.timeStamp;
        this.packetMessageData = o.packetMessageData;
    }

    @Override
    public String packetMessage() {
        return packetMessageData;
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

    public String getPacketMessageData() {
        return packetMessageData;
    }

    public void setPacketMessageData(String packetMessageData) {
        this.packetMessageData = packetMessageData;
    }
}
