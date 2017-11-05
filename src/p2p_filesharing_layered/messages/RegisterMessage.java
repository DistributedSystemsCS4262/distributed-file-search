package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;

public class RegisterMessage extends RequestMessage {
    private String name;
    public RegisterMessage(String name){
        super("REG",Constants.BOOTSTRAP_SERVER_IP,Constants.BOOTSTRAP_SERVER_PORT);
        this.name = name;
    }

    @Override
    public String packetMessage() {
        String join_msg = super.getAction() +" " + Constants.IP_ADDRESS + " " + Constants.PORT+ " " + this.name;
        join_msg = Utility.getMessageLength(join_msg) + " " + join_msg;
        return join_msg;
    }
}
