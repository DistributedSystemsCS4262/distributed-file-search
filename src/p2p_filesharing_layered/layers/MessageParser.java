package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.messages.*;

import java.net.DatagramPacket;
import java.util.HashSet;
import java.util.StringTokenizer;

public class MessageParser {
    private Messenger messenger;
    private Node node;
    private HashSet<String> requests;


    public MessageParser(Node node) {
        this.node = node;
        messenger = new Messenger(this);
        this.requests = new HashSet<String>();
    }

    public void parseMessage(RequestMessage requestMessage){

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
                ReceiveResponseMessage receiveResponseMessage = new ReceiveResponseMessage("UNROK",token,"Unregister");
                messenger.receiveMessage(receiveResponseMessage);
                break;
            case "JOIN":
                RequestMessage requestMessage = new RequestMessage("JOIN",token);
                messenger.receiveMessage(requestMessage);
                break;
            case "JOINOK":
                ReceiveResponseMessage joinReceiveResponseMessage = new ReceiveResponseMessage("JOINOK",token,"Join",packet.getAddress().getHostAddress(), packet.getPort());
                messenger.receiveMessage(joinReceiveResponseMessage);
                break;
            case "LEAVE":
                RequestMessage leaveRequestMessage = new RequestMessage("LEAVE",token);
                messenger.receiveMessage(leaveRequestMessage);
                break;
            case "LEAVEOK":
                ReceiveResponseMessage leaveReceiveResponseMessage = new ReceiveResponseMessage("LEAVEOK",token,"Leave",packet.getAddress().getHostAddress(), packet.getPort());
                messenger.receiveMessage(leaveReceiveResponseMessage);
                break;
            case "DISC":
                if(requests.isEmpty() || !requests.contains(data)){
                    requests.add(data);
                    OtherDiscoverMessage otherDiscoverMessage = new OtherDiscoverMessage(data);
                    messenger.receiveMessage(otherDiscoverMessage);
                }
                break;
            case "DISCACK":
                DiscoverMessage discoverMessage = new DiscoverMessage(data);
                messenger.receiveMessage(discoverMessage);
                break;
            case "SER":
                SearchMessage searchMessage = new SearchMessage(data);
                messenger.receiveMessage(searchMessage);
                break;
            case "SEROK":
                System.out.println(data);
//                this.search_ok(token);
                break;
            case "NEXT":
                RequestMessage requestMessege=new RequestMessage("NEXT", token);
                messenger.receiveMessage(requestMessege);
                break;
            case "NEXTOK":
                //compose a next ok message
                // send to messenger .receive
                ReceiveResponseMessage nextOkMessage=new ReceiveResponseMessage("NEXTOK",token,data);
                nextOkMessage.setIp(packet.getAddress().getHostAddress());
                nextOkMessage.setPort(packet.getPort());
                messenger.receiveMessage(nextOkMessage);
                break;
            case "ERROR":
//                this.error();
                break;
            default:
                break;
        }
    }

    public void sendMessage(RequestMessage requestMessage) {
        node.send(requestMessage.packetMessage(), requestMessage.getIp(), requestMessage.getPort());
    }
}
