package p2p_filesharing_layered.layers;

import p2p_filesharing.SearchFile;
import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.Message;
import p2p_filesharing_layered.messages.RegisterMessage;
import p2p_filesharing_layered.messages.RegisterOkMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller extends Thread {
    private Messenger messenger;

    public Controller(Messenger messenger){
        this.messenger = messenger;
        this.start();
    }

    public void join(List<Neighbour> new_neighbours) {
        for (Neighbour neighbour : new_neighbours) {
            messenger.sendMessage(new Message("JOIN",neighbour.getIp(), neighbour.getPort()));
        }
    }

    public void register(){
        messenger.sendMessage(new RegisterMessage("VINUJAN"));
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            synchronized (System.out) {
                System.out.println("Enter command");
                System.out.println("r : Register with bootstrap server");
                System.out.println("u : Unregister from bootstrap server");
                // System.out.println("j : join ");
                System.out.println("l : leave distributed system");
                System.out.println("s : serch file");
                System.out.println("p : print neighbours");
            }

            String in = scanner.nextLine().toLowerCase();

            if (in.equals("r")) {

                register();
//                new Thread(listner).start();

            } else if (in.equals("u")) {
//                node.UnRegister();

            } else if (in.equals("j")) {

            } else if (in.equals("l")) {
//                node.leave();

            } else if (in.equals("s")) {
//                SearchFile ser = new SearchFile(node,datagramSocket);
//                System.out.println("Enter file name:");
//                String filename = scanner.nextLine();
//                filename= "\""+filename+"\"";
//                for(p2p_filesharing.Neighbour s:node.getNeighbours()){
//                    ser.initiateSearchMsg(String.valueOf(System.currentTimeMillis()), s.getIp(), s.getPort(), filename);
//                }
            } else if (in.equals("p")) {
                //print neighbours
//                node.print_neighbours();
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
}
