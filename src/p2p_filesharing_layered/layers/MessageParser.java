package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.messages.*;

import java.net.DatagramPacket;
import java.util.HashSet;
import java.util.StringTokenizer;
import p2p_filesharing_layered.Constants;

public class MessageParser {

    private Messenger messenger;
    private UDP node;
    private HashSet<String> requests;
    private int inMsg =0;
    private int outMsg =0;
    private int discMsgSent = 0;
    private int discMsgReci = 0;
    
    public MessageParser(UDP node) {
        this.node = node;
        messenger = new Messenger(this);
        this.requests = new HashSet<String>();
    }

    public void parseMessage(RequestMessage requestMessage) {

    }

    public void parseMessage(String message, String ip, int port) {
        // handle data , implement parser
        System.out.println("Parsing msg TCP" + message);
        
        StringTokenizer token = new StringTokenizer(message);
        String lenght = token.nextToken();
        String command = token.nextToken();
        System.out.println(command);
        //inMsg++;
        //System.out.println("Message count - In: "+inMsg);
//        SearchFile ser = new SearchFile(this.node,this.socket);
        switch (command) {
            case "REGOK":
                RegisterOkMessage registerOkMessage = new RegisterOkMessage(token);
                messenger.receiveMessage(registerOkMessage);
                break;
            case "UNROK":
                ReceiveResponseMessage receiveResponseMessage = new ReceiveResponseMessage("UNROK", token, "Unregister");
                messenger.receiveMessage(receiveResponseMessage);
                break;
            case "JOIN":
                RequestMessage requestMessage = new RequestMessage("JOIN", token);
                messenger.receiveMessage(requestMessage);
                break;
            case "JOINOK":
                ReceiveResponseMessage joinReceiveResponseMessage = new ReceiveResponseMessage("JOINOK", token, "Join", ip, port);
                messenger.receiveMessage(joinReceiveResponseMessage);
                break;
            case "LEAVE":
                RequestMessage leaveRequestMessage = new RequestMessage("LEAVE", token);
                messenger.receiveMessage(leaveRequestMessage);
                break;
            case "LEAVEOK":
                ReceiveResponseMessage leaveReceiveResponseMessage = new ReceiveResponseMessage("LEAVEOK", token, "Leave", ip, port);
                messenger.receiveMessage(leaveReceiveResponseMessage);
                break;
            case "DISC":
                String messageCopy = message.trim();
                String msg[]=messageCopy.split(" ");
                messageCopy ="";
                for(int i=0;i<msg.length-1;i++){
                    messageCopy += msg[i]+" ";
                }
                messageCopy = messageCopy.trim();
                //discMsgReci++;
                //System.out.println("Discovery messages received: "+discMsgReci);
                messenger.getDiscReci();
                if (requests.isEmpty() || !requests.contains(messageCopy)) {
                    requests.add(messageCopy);
                    OtherDiscoverMessage otherDiscoverMessage = new OtherDiscoverMessage(message);
                    messenger.receiveMessage(otherDiscoverMessage);
                }
                break;
            case "DISCACK":
                DiscoverMessage discoverMessage = new DiscoverMessage(message);
                messenger.receiveMessage(discoverMessage);
                break;
            case "SER":
                SearchMessage searchMessage = new SearchMessage(message);
                messenger.receiveMessage(searchMessage);
                break;
            case "SEROK":
                System.out.println(message);
                ReceiveResponseMessage serOk=new ReceiveResponseMessage("SEROK", token, message);
                serOk.setIp(ip);
                serOk.setPort(port);
                messenger.receiveMessage(serOk);
                // send it to messenger-> controller
//                this.search_ok(token);
                break;
            case "NEXT":
                RequestMessage requestMessege = new RequestMessage("NEXT", token);
                messenger.receiveMessage(requestMessege);
                break;
            case "NEXTOK":
                //compose a next ok message
                // send to messenger .receive
                ReceiveResponseMessage nextOkMessage = new ReceiveResponseMessage("NEXTOK", token, message);
                nextOkMessage.setIp(ip);
                nextOkMessage.setPort(port);
                messenger.receiveMessage(nextOkMessage);
                break;
            case "ALIVE":
                AliveMessage alive = new AliveMessage("ALIVE", token);
                messenger.receiveMessage(alive);
                break;
            case "ISALIVE":
                IsAliveMessage isAlive = new IsAliveMessage("ISALIVE", token);
                messenger.receiveMessage(isAlive);
                break;
            case "ERROR":
//                this.error();
                break;
            default:
                break;
        }
    }

    public void parseMessage(DatagramPacket packet) {
        byte[] dataRaw = packet.getData();
        String data = new String(dataRaw, 0, packet.getLength());
        // handle data , implement parser
        System.out.println("Parsing msg UDP" + data);
        StringTokenizer token = new StringTokenizer(data);
        String lenght = token.nextToken();
        String command = token.nextToken();
//        SearchFile ser = new SearchFile(this.node,this.socket);
        //inMsg++;
        //System.out.println("Message count - In: "+inMsg);
        switch (command) {
            case "REGOK":
                RegisterOkMessage registerOkMessage = new RegisterOkMessage(token);
                messenger.receiveMessage(registerOkMessage);
                break;
            case "UNROK":
                ReceiveResponseMessage receiveResponseMessage = new ReceiveResponseMessage("UNROK", token, "Unregister");
                messenger.receiveMessage(receiveResponseMessage);
                break;
            case "JOIN":
                RequestMessage requestMessage = new RequestMessage("JOIN", token);
                messenger.receiveMessage(requestMessage);
                break;
            case "JOINOK":
                ReceiveResponseMessage joinReceiveResponseMessage = new ReceiveResponseMessage("JOINOK", token, "Join", packet.getAddress().getHostAddress(), packet.getPort());
                messenger.receiveMessage(joinReceiveResponseMessage);
                break;
            case "LEAVE":
                RequestMessage leaveRequestMessage = new RequestMessage("LEAVE", token);
                messenger.receiveMessage(leaveRequestMessage);
                break;
            case "LEAVEOK":
                ReceiveResponseMessage leaveReceiveResponseMessage = new ReceiveResponseMessage("LEAVEOK", token, "Leave", packet.getAddress().getHostAddress(), packet.getPort());
                messenger.receiveMessage(leaveReceiveResponseMessage);
                break;
            case "DISC":
                String messageCopy = data.trim();
                String msg[]=messageCopy.split(" ");
                messageCopy ="";
                for(int i=0;i<msg.length-1;i++){
                    messageCopy += msg[i]+" ";
                }
                messageCopy = messageCopy.trim();
                //discMsgReci++;
                //System.out.println("Discovery messages received: "+discMsgReci);
                messenger.getDiscReci();
                if (requests.isEmpty() || !requests.contains(messageCopy)) {
                    requests.add(messageCopy);
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
                ReceiveResponseMessage serOk=new ReceiveResponseMessage("SEROK", token, data);
                serOk.setIp(packet.getAddress().getHostAddress());
                serOk.setPort(packet.getPort());
                messenger.receiveMessage(serOk);
                 // send it to messenger-> controller
//                this.search_ok(token);
                break;
            case "NEXT":
                RequestMessage requestMessege = new RequestMessage("NEXT", token);
                messenger.receiveMessage(requestMessege);
                break;
            case "NEXTOK":
                //compose a next ok message
                // send to messenger .receive
                ReceiveResponseMessage nextOkMessage = new ReceiveResponseMessage("NEXTOK", token, data);
                nextOkMessage.setIp(packet.getAddress().getHostAddress());
                nextOkMessage.setPort(packet.getPort());
                messenger.receiveMessage(nextOkMessage);
                break;
            case "ALIVE":
                AliveMessage alive = new AliveMessage("ALIVE", token);
                messenger.receiveMessage(alive);
                break;
            case "ISALIVE":
                IsAliveMessage isAlive = new IsAliveMessage("ISALIVE", token);
                messenger.receiveMessage(isAlive);
                break;
            case "ERROR":
//                this.error();
                break;
            default:
                break;
        }
    }

    public void sendMessage(RequestMessage requestMessage) {
        //outMsg++;
        //System.out.println("Message count - Out: "+outMsg);
        if(requestMessage.getAction().equals("DISC")){
            //discMsgSent++;
            messenger.getDiscSent();
            //System.out.println("Discovery Messages Sent: "+discMsgSent);
        }
        node.send(requestMessage.packetMessage(), requestMessage.getIp(), requestMessage.getPort());
        
    }
    
    public int getDiscSent(){
        return discMsgSent;
    }
    
    public int getDiscReci(){
        return discMsgReci;
    }
}
