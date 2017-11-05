package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;

import java.util.StringTokenizer;

public class SearchMessage extends RequestMessage {
    private String fileName;
    private int hops;

    public SearchMessage(String receivedData) {
        StringTokenizer token = new StringTokenizer(receivedData);
        String length = token.nextToken();
        setAction(token.nextToken());
        setIp(token.nextToken());
        setPort(Integer.parseInt(token.nextToken()));
        token.nextToken();
        this.hops = Integer.parseInt(token.nextToken());
        token = new StringTokenizer(receivedData, "\"");
        token.nextToken();
        this.fileName = token.nextToken();
    }

    public SearchMessage(String action, String ip, int port, String fileName, int hops) {
        super(action, ip, port);
        this.fileName = fileName;
        this.hops = hops;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    @Override
    public String packetMessage() {
        String join_msg = getAction() + " " + Constants.IP_ADDRESS + " " + Constants.PORT + " \"" + this.fileName + "\" " + this.hops;
        join_msg = Utility.getMessageLength(join_msg) + " " + join_msg;
        return join_msg;
    }
}
