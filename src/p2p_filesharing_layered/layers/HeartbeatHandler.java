/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing_layered.layers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.AliveMessage;
import p2p_filesharing_layered.messages.IsAliveMessage;
import p2p_filesharing_layered.messages.RegisterMessage;
import p2p_filesharing_layered.messages.RequestMessage;

/**
 *
 * @author ChathurangaKCD
 */
public class HeartbeatHandler extends Observable {

    private static final int HEARTBEAT_INTERVAL = 1000 * 60;
    private static final int PING_TIMEOUT = 2000;
    private static final int REQUEST_TIMEOUT = 2000;
    private static final int NO_OF_SUCCESSORS_TO_CHECK = 6;
    private final Neighbour self;
    private final List<Neighbour> neighbours;
    private final ConcurrentMap<Integer, Set<Neighbour>> succesors;
    private final Messenger messenger;

    private ConcurrentHashMap<Neighbour, Boolean> aliveNodes;
    private ConcurrentHashMap<Neighbour, Neighbour> deadNodeSuccesors; //hold successors of dead nodes until they respond

    private static boolean isAlone;

    HeartbeatHandler(Messenger messenger, List<Neighbour> neighbours, ConcurrentMap<Integer, Set<Neighbour>> succesors) {
        this.neighbours = neighbours;
        this.succesors = succesors;
        this.messenger = messenger;
        self = new Neighbour(Constants.IP_ADDRESS, Constants.PORT);
    }
    private final Thread handler = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    System.out.println("Heartbeat cycle started..");
                    isAlone = true;
                    checkNeighbours();
                    Thread.sleep(REQUEST_TIMEOUT);
                    checkDeadNodeSuccesors();
                    aliveNodes = null;
                    Thread.sleep(REQUEST_TIMEOUT);
                    deadNodeSuccesors = null;
                    System.out.println("Heartbeat cycle ended..");
                    handleIsolation();
                } catch (InterruptedException ex) {

                }
            }
        }
    };

    public void start() {
        handler.start();
    }

    /**
     * returns n successors from a node, null if there aren't any valid
     * successors
     *
     * @param node
     * @return
     */
    private Neighbour[] getSuccessors(Neighbour node) {
        Neighbour[] successors = new Neighbour[NO_OF_SUCCESSORS_TO_CHECK];
        Set<Neighbour> set = succesors.get(node.hashCode());
        int i = 0;
        if (set != null) {
            for (Neighbour n : set) {
                if (aliveNodes.containsKey(n) || self.equals(n)) {
                    continue; //skip successor nodes that are already in neighbours list.
                }
                successors[i] = n;
                i++;
                if (i >= NO_OF_SUCCESSORS_TO_CHECK) {
                    break;
                }
            }
        }
        if (i == 0) {
            System.out.println("No succesors of dead " + node);
            return null;
        }
        return successors;
    }

    private void checkNeighbours() {
        List<Neighbour> neighboursList = neighbours;
        aliveNodes = new ConcurrentHashMap<>();
        for (Neighbour neighbour : neighboursList) {
            if (checkNodeHeartbeat(neighbour)) {
                checkServiceHeartbeat(neighbour, false);
                aliveNodes.put(neighbour, Boolean.FALSE);
            }
        }
    }

    private void checkDeadNodeSuccesors() {
        deadNodeSuccesors = new ConcurrentHashMap<>();
        for (Map.Entry<Neighbour, Boolean> entry : aliveNodes.entrySet()) {
            Neighbour node = entry.getKey();
            Boolean alive = entry.getValue();
            if (!alive) {
                System.out.println("Dead " + node);
                Neighbour[] temp = getSuccessors(node);
                if (temp == null) {//remove node  if there aren't any successors
                    removeDeadNode(node);
                    continue;
                }
                for (int i = 0; i < NO_OF_SUCCESSORS_TO_CHECK; i++) {
                    if (temp[i] == null) {
                        break;
                    }
                    deadNodeSuccesors.put(temp[i], node);
                    if (checkNodeHeartbeat(temp[i])) {
                        checkServiceHeartbeat(temp[i], true);
                    }
                }
            }
        }
    }

    boolean checkNodeHeartbeat(Neighbour node) {
        boolean b = false;
        try {
            InetAddress address = InetAddress.getByName(node.getIp());

            for (int i = 0; i < 3; i++) {
                b |= address.isReachable(PING_TIMEOUT);
            }
        } catch (IOException e) {
        }
        return b;
    }

    boolean checkServiceHeartbeat(Neighbour node, boolean successor) {
        RequestMessage requestMessage = new IsAliveMessage(node);
        for (int i = 0; i < 3; i++) {
            messenger.sendMessage(requestMessage);
        }
        System.out.println("Heartbeat request sent to " + node + (successor ? " (Succesor)" : ""));
        return false;
    }

    /**
     * update heartbeat status. update neighbour list if the sender is a
     * successor of a dead node.
     *
     * @param response
     */
    public void handleIsAliveResponse(AliveMessage response) {
        Neighbour succesorNode = new Neighbour(response.getIp(), response.getPort());
        System.out.println("Heartbeat response recieved from " + succesorNode);
        if (aliveNodes != null && aliveNodes.containsKey(succesorNode)) {
            isAlone = false;
            aliveNodes.put(succesorNode, Boolean.TRUE);
        } else if (deadNodeSuccesors != null) {
            if (deadNodeSuccesors.containsKey(succesorNode)) {
                isAlone = false;
                Neighbour deadNode = deadNodeSuccesors.get(succesorNode);
                if (removeDeadNode(deadNode)) {//remove deadNode form neighbour list
                    joinWithSuccessor(succesorNode); //join with successor unless had joined with another succesor
                }
            }
        }
    }

    public void handleIsAliveRequest(IsAliveMessage request) {
        AliveMessage response = new AliveMessage(request.getIp(), request.getPort());
        messenger.sendMessage(response);
    }

    //Controller functions
    private boolean removeDeadNode(Neighbour deadNode) {
        boolean removed = false;
        synchronized (neighbours) {
            removed = neighbours.remove(deadNode);
        }
        System.out.println("dead node removed " + deadNode);
        return removed;
    }

    private void handleIsolation() {
        if (isAlone) {
            System.out.println("Alone :'(");
            //unregister
            RequestMessage requestMessage = new RegisterMessage("VINUJAN");
            requestMessage.setAction("UNREG");
            messenger.sendMessage(requestMessage);
            //register
            messenger.sendMessage(new RegisterMessage("VINUJAN"));
        }
    }

    private void joinWithSuccessor(Neighbour succesorNode) {
        System.out.println("Join succesor" + succesorNode);
        messenger.sendMessage(new RequestMessage("JOIN", succesorNode.getIp(), succesorNode.getPort()));
    }
}
