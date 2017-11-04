package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.Message;
import p2p_filesharing_layered.messages.RegisterOkMessage;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MessageParser {
    private Messenger messenger;
    private Node node;

    public MessageParser(Node node) {
        this.node = node;
        messenger = new Messenger(this);
    }

    public void parseMessage(Message message){

    }

    public void parseMessage(DatagramPacket packet) {
        byte[] dataRaw = packet.getData();
        String data = new String(dataRaw, 0, packet.getLength());
        // handle data , implement parser
        System.out.println("Parsing msg "+data);
        StringTokenizer token = new StringTokenizer(data);
        String lenght = token.nextToken();
        String command = token.nextToken();
//        SearchFile ser = new SearchFile(this.node,this.socket);
        switch (command) {
            case "REGOK":
                RegisterOkMessage registerOkMessage = new RegisterOkMessage(token);
                messenger.receiveMessage(registerOkMessage);
                break;
            case "UNROK":
//                this.unregister_ok(token);
                break;
            case "JOIN":
//                this.join(token);
                break;
            case "JOINOK":
//                this.join_ok(token, packet);
                break;
            case "LEAVE":
//                this.leave(token);
                break;
            case "LEAVEOK":
//                this.leave_ok(token, packet);
                break;
            case "DISC":
//                ser.searchDiscMsgRecieved(data);
                break;
            case "DISCACK":
//                ser.search(data);
                break;
            case "SER":
//                ser.searchMsgRecieved(data);
                //this.search_ok();
                break;
            case "SEROK":
                System.out.println(data);
//                this.search_ok(token);
                break;
            case "ERROR":
//                this.error();
                break;
            default:
                break;
        }
    }

    public void sendMessage(Message message) {
        node.send(message.packetMessage(),message.getIp(),message.getPort());
    }
}
