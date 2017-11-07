package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Utility;

public class SendResponseMessage extends RequestMessage {
    private int value ;


    public SendResponseMessage(String action, String ip, int port,int value) {
        super(action, ip, port);
        this.value = value;
    }

    @Override
    public String packetMessage() {
        String join_msg = getAction() +" " + this.value;
        join_msg = Utility.getMessageLength(join_msg) + " " + join_msg;
        return join_msg;
    }
}
