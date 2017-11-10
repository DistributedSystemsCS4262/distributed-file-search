package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.messages.*;

public class Messenger {
    private MessageParser messageParser;
    private Controller controller;

    public Messenger(MessageParser messageParser){
        controller = new Controller(this);
        this.messageParser = messageParser;
    }

    public void receiveMessage(RegisterOkMessage registerOkMessage){
        controller.handleRegisterOkResponse(registerOkMessage);
    }

    public void receiveMessage(ReceiveResponseMessage receiveResponseMessage){
        if (receiveResponseMessage.getAction().equals("NEXTOK")){
            controller.handleGetSuccessorsOkResponse(receiveResponseMessage);
            return;
        }else if(receiveResponseMessage.getAction().equals("SEROK")){
                System.out.println("serch ok :"+receiveResponseMessage.getDescription());
            controller.handleSearchOk(receiveResponseMessage);
            return;
            }
        
        if(receiveResponseMessage.getValue().equals("0")){
            System.out.println(receiveResponseMessage.getDescription()+" success!");
            if(receiveResponseMessage.getAction().equals("LEAVEOK")){
                controller.handleLeaveOkResponse(receiveResponseMessage);
            } else if (receiveResponseMessage.getAction().equals("JOINOK")) {
                controller.handleJoinOkResponse(receiveResponseMessage);
            } else if (receiveResponseMessage.getAction().equals("UNROK")){
            controller.handleUnrOkResponse(receiveResponseMessage);
            }
        }else if(receiveResponseMessage.getValue().equals("9999")){
            System.out.println("error while "+ receiveResponseMessage.getDescription()+" !");
        }else{
            System.out.println("Something gone wrong when " + receiveResponseMessage.getDescription() + " !");
        }
    }

    public void receiveMessage(RequestMessage requestMessage){
        if(requestMessage.getAction().equals("JOIN")){
            controller.handleJoinRequest(requestMessage);
        }else if(requestMessage.getAction().equals("LEAVE")){
            controller.handleLeaveRequest(requestMessage);
        }else if(requestMessage.getAction().equals("DISC")){
            controller.handleDiscoverRequest((OtherDiscoverMessage)requestMessage);
        }else if(requestMessage.getAction().equals("DISCACK")){
            controller.handleDiscoverAckRequest((DiscoverMessage)requestMessage);
        }else if(requestMessage.getAction().equals("SER")){
            controller.handleSearchRequest((SearchMessage)requestMessage);
        }else if(requestMessage.getAction().equals("NEXT")){
             controller.handleGetSuccessorsRequest(requestMessage);
        }else if(requestMessage.getAction().equals("ALIVE")){
            controller.handleAliveRequest((AliveMessage)requestMessage);
        }else if(requestMessage.getAction().equals("ISALIVE")){
             controller.handleIsAliveMessege((IsAliveMessage)requestMessage);
        }
    }

    public void sendMessage(RequestMessage requestMessage) {
        this.messageParser.sendMessage(requestMessage);
    }
}
