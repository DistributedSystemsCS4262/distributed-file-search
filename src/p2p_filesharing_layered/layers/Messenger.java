package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.messages.Message;
import p2p_filesharing_layered.messages.RegisterOkMessage;

public class Messenger {
    private MessageParser messageParser;
    private Controller controller;

    public Messenger(MessageParser messageParser){
        controller = new Controller(this);
        this.messageParser = messageParser;
    }

    public void receiveMessage(RegisterOkMessage registerOkMessage){
        controller.join(registerOkMessage.getNeighbours());
    }

    public void sendMessage(Message message) {
        this.messageParser.sendMessage(message);
    }
}
