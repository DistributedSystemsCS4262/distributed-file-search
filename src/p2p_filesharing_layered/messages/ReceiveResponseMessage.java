package p2p_filesharing_layered.messages;

import java.util.StringTokenizer;

public class ReceiveResponseMessage extends RequestMessage {
    private String value;
    private String description;

    public ReceiveResponseMessage(String action,StringTokenizer stringTokenizer, String description,String ip, int port) {
        super(action,ip,port);
        this.value = stringTokenizer.nextToken();
        this.description = description;
    }

    public ReceiveResponseMessage(String action,StringTokenizer stringTokenizer, String description) {
        super(action);
        this.value = stringTokenizer.nextToken();
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
