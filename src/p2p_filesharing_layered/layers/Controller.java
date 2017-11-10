package p2p_filesharing_layered.layers;

import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.interfaces.MainUI;

public class Controller extends Thread {

    private Messenger messenger;
    private FileSystem fileSystem;
    private Set<Neighbour> neighbours;
    private ConcurrentMap<Integer, Set<Neighbour>> succesors;
    private MainUI userInterface;
    private HeartbeatHandler hbHandler;

    public Controller(Messenger messenger) {
        this.messenger = messenger;
        this.fileSystem = new FileSystem();
        neighbours = new HashSet<>();
        this.succesors = new ConcurrentHashMap<Integer, Set<Neighbour>>();
        userInterface=new MainUI(this);
        hbHandler = new HeartbeatHandler(this.messenger, this);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               userInterface.setVisible(true);
            }
        });
        hbHandler.start();
        //this.start();
    }
    
    public void printAllFiles(){
        for(String file:fileSystem.getAllFiles()){
            System.out.println(file);
        }
    }

    public void handleRegisterOkResponse(RegisterOkMessage registerOkMessage) {
        
        userInterface.updateInterface("Successfully Registered with the Server \n"+Constants.IP_ADDRESS + " : " + Constants.PORT + "\n");
        for (Neighbour neighbour : registerOkMessage.getNeighbours()) {
            messenger.sendMessage(new RequestMessage("JOIN", neighbour.getIp(), neighbour.getPort()));
        }
    }

    public void register() {
        
        messenger.sendMessage(new RegisterMessage("VINUJAN"));
       
    }

    public void unregister() {
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
                for(String file:fileSystem.getAllFiles()){
                  System.out.println(file);
                }
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
                initiateSearch(scanner.nextLine());
//                    ser.initiateSearchMsg(String.valueOf(System.currentTimeMillis()), s.getIp(), s.getPort(), filename);
            } else if (in.equals("p")) {
                printNeighbours();
            } else if (in.equals("f")) {
                printFiles();
            } else if (in.equals("e")) {
                //print neighbours
//                System.exit(0);
            } else {

            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
    }

    public String printFiles() {
        String output="\nLIST OF FILES\n";
        List<String> files = fileSystem.getAllFiles();
        for (String file : files) {
            System.out.println(file);
            output+= "\t" +file+"\n";
        }
        
        return output;
    }

    public void initiateSearch(String fileName) {
        if (fileName.isEmpty()) {
            System.out.println("Search filename is blank");
            return;
        }
        fileName = "\"" + fileName + "\"";
        for (Neighbour neighbour : neighbours) {
            messenger.sendMessage(new DiscoverMessage("DISC", neighbour.getIp(), neighbour.getPort(), fileName, String.valueOf(System.currentTimeMillis()),1));
        }
    }

    public String printNeighbours() {
        
        String output="";
        synchronized (System.out) {
            System.out.println("set of neighbours");
            output="\n\nSET OF NEIGHBOURS:\n";
            if (neighbours.isEmpty()) {
                System.out.println("\nNo neighbours");
                output+="\tNO NEIGHBOURS FOUND";
                return output;
            }
            for (Neighbour neighbour : neighbours) {
                System.out.println(String.format("\t\tIp %s : Port %d", neighbour.getIp(), neighbour.getPort()));
                output+="\t " + neighbour.getIp() + " : "+ neighbour.getPort() + "\n";
            }
            
            System.out.println("\nset of sucessors");
            output+="\nSET OF SUCCESSORS\n";
            for (int key :succesors.keySet()){
                System.out.println("key : "+key);
                output+="key : "+key +"\n";
                for(Neighbour neighbour: succesors.get(key)){
                    System.out.println("\t"+neighbour.getIp()+" : "+neighbour.getPort() );
                    output+="\t"+neighbour.getIp()+" : "+neighbour.getPort()+ "\n";
                }
            }
        }
        return output;
    }

    public void leave() {
        for (Neighbour neighbour : neighbours) {
            messenger.sendMessage(new RequestMessage("LEAVE", neighbour.getIp(), neighbour.getPort()));
        }
    }

    public void handleJoinOkResponse(ReceiveResponseMessage receiveResponseMessage) {
        neighbours.add(new Neighbour(receiveResponseMessage.getIp(), receiveResponseMessage.getPort()));
        String output="Connected witht the Neighbour : \t" +receiveResponseMessage.getIp()+ " : " +receiveResponseMessage.getPort() +"\n";
        userInterface.updateInterface(output);
        messenger.sendMessage(new RequestMessage("NEXT", receiveResponseMessage.getIp(), receiveResponseMessage.getPort()));
    }

    public void handleLeaveOkResponse(ReceiveResponseMessage receiveResponseMessage) {
        neighbours.remove(new Neighbour(receiveResponseMessage.getIp(), receiveResponseMessage.getPort()));
        String output="Disconnected witht the Neighbour : \t" +receiveResponseMessage.getIp()+ " : " +receiveResponseMessage.getPort() +"\n";
        userInterface.updateInterface(output);
    }

    public void handleJoinRequest(RequestMessage requestMessage) {
        int value = 0;
        try {
            neighbours.add(new Neighbour(requestMessage.getIp(), requestMessage.getPort()));
        } catch (Exception ex) {
            value = 9999;
        }
        messenger.sendMessage(new SendResponseMessage("JOINOK", requestMessage.getIp(), requestMessage.getPort(), value));
    }

    public void handleLeaveRequest(RequestMessage requestMessage) {
        int value = 0;
        try {
            neighbours.remove(new Neighbour(requestMessage.getIp(), requestMessage.getPort()));
        } catch (Exception ex) {
            value = 9999;
        }
        messenger.sendMessage(new SendResponseMessage("LEAVEOK", requestMessage.getIp(), requestMessage.getPort(), value));
    }

    public void handleDiscoverRequest(OtherDiscoverMessage o) {
        messenger.sendMessage(new DiscoverMessage("DISCACK", o.getIp(), o.getPort(), o.getFileName(), o.getTimeStamp(),o.getHopCount()));
        for (Neighbour neighbour : this.neighbours) {
            if (!(neighbour.getIp().equals(o.getIp()) && neighbour.getPort() == o.getPort())) {
                messenger.sendMessage(new OtherDiscoverMessage(o, neighbour.getIp(), neighbour.getPort()));
            }
        }
    }

    public void handleDiscoverAckRequest(DiscoverMessage d) {
        messenger.sendMessage(new SearchMessage("SER", d.getIp(), d.getPort(), d.getFileName(), 0));
    }

    public void handleSearchOk(){
        String output="\nFile you searched :";
        //if no of hits>0
        output+="\nIP: Port: Hops: No of Hits:";
        //else
        output+="FIlE NOT FOUND";
        //for 9999 and 9998
    userInterface.updateInterface(output);
    
    }
    
    public void handleSearchRequest(SearchMessage searchMessage) {
        StringTokenizer token = new StringTokenizer(searchMessage.getFileName());
        String word = token.nextToken();

        List<String> result = fileSystem.searchFiles(word);

        if (result != null) {
            while (token.hasMoreTokens()) {
                word = token.nextToken();
                result.retainAll(fileSystem.searchFiles(word));
            }
        }
        ArrayList<String> filelist = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            String searchString = searchMessage.getFileName().toUpperCase().replace(" ", "#") + "#";
            for (String file : result) {
                String fileModified = file.toUpperCase().replace(" ", "#") + "#";
                if (fileModified.contains(searchString)) {
                    filelist.add(file);
                }
            }
        }
        SearchOkMessage searchOkMessage = new SearchOkMessage("SEROK", searchMessage.getIp(), searchMessage.getPort(), filelist);
        messenger.sendMessage(searchOkMessage);
    }

    void handleGetSuccessorsRequest(RequestMessage requestMessage) {

        //send nextok messege
        //send array of neighbours as well 
        ArrayList<Neighbour> successors = new ArrayList<>();

        ArrayList<Neighbour> neighbourCopy = new ArrayList<>();

        // copy neighbours
        for (Neighbour neighbour : neighbours) {
            neighbourCopy.add(
                    new Neighbour(neighbour.getIp(), neighbour.getPort())
            );
        }
        neighbourCopy.remove(new Neighbour(Constants.IP_ADDRESS, Constants.PORT));

        int number = Math.min(3, neighbourCopy.size()); // take minimum of 3 or neighbour size
        Collections.shuffle(neighbourCopy);
        for (int i = 0; i < number; i++) {
            successors.add(neighbourCopy.get(i));
        }

        messenger.sendMessage(new SuccessorOkMessage("NEXTOK", requestMessage, successors));

    }

    void handleGetSuccessorsOkResponse(ReceiveResponseMessage receiveResponseMessage) {
        System.out.println(receiveResponseMessage.getDescription());
        String[] message = receiveResponseMessage.getDescription().split(" ", 4);

        if (Integer.parseInt(message[2]) > 0) {
            StringTokenizer succesorDetails = new StringTokenizer(message[3]);
            int key = (receiveResponseMessage.getIp() + ":" + receiveResponseMessage.getPort()).hashCode();
            Set<Neighbour> succesorsLocal = ConcurrentHashMap.newKeySet();
            
            
            
            while (succesorDetails.hasMoreTokens()) {                
                succesorsLocal.add(new Neighbour(succesorDetails.nextToken(), Integer.parseInt(succesorDetails.nextToken())));
            }
            //remove neighbours
            for(Neighbour neighbour : neighbours){
            succesorsLocal.remove(new Neighbour(neighbour.getIp(),neighbour.getPort()));           
            }
            
            
            if (!succesors.containsKey(key)) {
                succesors.put(key, succesorsLocal);
            } else {
                succesors.get(key).addAll(succesorsLocal);
            }
        }
        //add suceesors to the hash map
    }

    void handleUnrOkResponse(ReceiveResponseMessage receiveResponseMessage) {
        userInterface.updateInterface("Successfully Unregistered with the Server \n"+Constants.IP_ADDRESS + " : " + Constants.PORT);
    }
    
    
   

    public ArrayList<Neighbour> getNeighbours() {
        ArrayList<Neighbour> new_neighbours = new ArrayList<>();

        //System.out.println("neighbour info statrs :");
        for (Neighbour neighbour : new ArrayList<>(neighbours)) {
            Neighbour nei = new Neighbour(neighbour.getIp(), neighbour.getPort());

            System.out.println("neighbour info :" + nei.getIp());
            new_neighbours.add(nei);
        }

        return new_neighbours;
    }
    
    public boolean removeDeadNeighbour(Neighbour neighbour){
        
        boolean remove=neighbours.remove(neighbour);
        System.out.println("dead node removed " + neighbour.getPort());
    return remove;
    
    
    }
    public ConcurrentMap<Integer, Set<Neighbour>> getSuccessors() {
        ConcurrentMap<Integer, Set<Neighbour>> new_successors = new ConcurrentHashMap<Integer, Set<Neighbour>>(succesors);

       // System.out.println("neighbour info statrs :");
        

        return new_successors;
    }
    
     void handleIsAliveMessege(IsAliveMessage messege) {
        hbHandler.handleIsAliveRequest(messege);
    }

    void handleAliveRequest(AliveMessage response) {
        hbHandler.handleIsAliveResponse(response);
    }
    
    
}
