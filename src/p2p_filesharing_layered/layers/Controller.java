package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.*;

import java.util.*;

public class Controller extends Thread {
    private Messenger messenger;
    private FileSystem fileSystem;
    private List<Neighbour> neighbours;

    public Controller(Messenger messenger){
        this.messenger = messenger;
        this.fileSystem = new FileSystem();
        neighbours = new ArrayList<>();
        this.start();
    }

    public void handleRegisterOkResponse(RegisterOkMessage registerOkMessage) {
        for (Neighbour neighbour : registerOkMessage.getNeighbours()) {
            messenger.sendMessage(new RequestMessage("JOIN",neighbour.getIp(), neighbour.getPort()));
        }
    }

    public void register(){
        messenger.sendMessage(new RegisterMessage("VINUJAN"));
    }

    public void unregister(){
        RequestMessage requestMessage = new RegisterMessage("VINUJAN");
        requestMessage.setAction("UNREG");
        messenger.sendMessage(requestMessage);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            synchronized (System.out) {
                System.out.println("Enter command");
                System.out.println("r : Register with bootstrap server");
                System.out.println("u : Unregister from bootstrap server");
                // System.out.println("j : joinWithNeighbours ");
                System.out.println("l : leave distributed system");
                System.out.println("s : initiateSearch file");
                System.out.println("p : print neighbours");
                System.out.println("f : print files");
            }

            String in = scanner.nextLine().toLowerCase();

            if (in.equals("r")) {
                register();
            } else if (in.equals("u")) {
                unregister();
            } else if (in.equals("j")) {

            } else if (in.equals("l")) {
                leave();
            } else if (in.equals("s")) {
//                SearchFile ser = new SearchFile(node,datagramSocket);
                System.out.println("Enter file name:");
                initiateSearch( scanner.nextLine());
//                    ser.initiateSearchMsg(String.valueOf(System.currentTimeMillis()), s.getIp(), s.getPort(), filename);
            } else if (in.equals("p")) {
                printNeighbours();
            } else if (in.equals("f")) {
                printFiles();
            } else {
                if (in.equals("e")) {
                    //print neighbours
//                System.exit(0);
                } else {

                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
    }

    private void printFiles() {
        List<String> files = fileSystem.getAllFiles();
        for(String file : files){
            System.out.println(file);
        }
    }

    private void initiateSearch(String fileName) {
        if(fileName.isEmpty()){
            System.out.println("Search filename is blank");
            return;
        }
        fileName = "\""+fileName+"\"";
        for(Neighbour neighbour : neighbours){
            messenger.sendMessage(new DiscoverMessage("DISC",neighbour.getIp(),neighbour.getPort(),fileName,String.valueOf(System.currentTimeMillis())));
        }
    }

    private void printNeighbours() {
        synchronized (System.out) {
            System.out.println("set of neighbours");
            if(neighbours.isEmpty()){
                System.out.println("No neighbours");
                return;
            }
            for (Neighbour neighbour : neighbours) {
                System.out.println(String.format("\t\tIp %s : Port %d", neighbour.getIp(), neighbour.getPort()));
            }
        }
    }

    private void leave() {
        for (Neighbour neighbour : neighbours) {
            messenger.sendMessage(new RequestMessage("LEAVE",neighbour.getIp(),neighbour.getPort()));
        }
    }

    public void handleJoinOkResponse(ReceiveResponseMessage receiveResponseMessage){
        neighbours.add(new Neighbour(receiveResponseMessage.getIp(),receiveResponseMessage.getPort()));
    }

    public void handleLeaveOkResponse(ReceiveResponseMessage receiveResponseMessage){
        neighbours.remove(new Neighbour(receiveResponseMessage.getIp(),receiveResponseMessage.getPort()));
    }

    public void handleJoinRequest(RequestMessage requestMessage) {
        int value = 0;
        try {
            neighbours.add(new Neighbour(requestMessage.getIp(), requestMessage.getPort()));
        } catch (Exception ex) {
            value = 9999;
        }
        messenger.sendMessage(new SendResponseMessage("JOINOK",requestMessage.getIp(),requestMessage.getPort(),value));
    }

    public void handleLeaveRequest(RequestMessage requestMessage) {
        int value = 0;
        try {
            neighbours.remove(new Neighbour(requestMessage.getIp(), requestMessage.getPort()));
        } catch (Exception ex) {
            value = 9999;
        }
        messenger.sendMessage(new SendResponseMessage("LEAVEOK",requestMessage.getIp(),requestMessage.getPort(),value));
    }

    public void handleDiscoverRequest(OtherDiscoverMessage o) {
        messenger.sendMessage(new DiscoverMessage("DISCACK",o.getIp(),o.getPort(),o.getFileName(),o.getTimeStamp()));
        for(Neighbour neighbour:this.neighbours){
            if (!(neighbour.getIp().equals(o.getIp()) && neighbour.getPort() == o.getPort())) {
                messenger.sendMessage(new OtherDiscoverMessage(o,neighbour.getIp(),neighbour.getPort()));
            }
        }
    }

    public void handleDiscoverAckRequest(DiscoverMessage d) {
        messenger.sendMessage(new SearchMessage("SER",d.getIp(),d.getPort(),d.getFileName(),0));
    }

    public void handleSearchRequest(SearchMessage searchMessage) {
        StringTokenizer token = new StringTokenizer(searchMessage.getFileName());
        String word = token.nextToken();

        List<String> result = fileSystem.searchFiles(word);

        if(result!=null){
            while(token.hasMoreTokens()){
                word = token.nextToken();
                result.retainAll(fileSystem.searchFiles(word));
            }
        }
        ArrayList<String> filelist = new ArrayList<>();
        if(result!=null&&!result.isEmpty()){
            String searchString = searchMessage.getFileName().replace(" ", "#")+"#";
            for(String file:result){
                String fileModified = file.replace(" ", "#")+"#";
                if(fileModified.contains(searchString)){
                    filelist.add(file);
                }
            }
        }
        SearchOkMessage searchOkMessage = new SearchOkMessage("SEROK", searchMessage.getIp(), searchMessage.getPort(), filelist);
        messenger.sendMessage(searchOkMessage);
    }
}
