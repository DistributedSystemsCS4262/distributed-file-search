/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing_layered.layers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import p2p_filesharing_layered.domain.Neighbour;
import p2p_filesharing_layered.messages.AliveMessage;
import p2p_filesharing_layered.messages.IsAliveMessage;
import p2p_filesharing_layered.messages.RequestMessage;

/**
 *
 * @author ChathurangaKCD
 */
public class HeartbeatHandler {

    private static final int HEARTBEAT_INTERVAL = 1000 * 60 * 5;
    private static final int PING_TIMEOUT = 2000;
    private static final int REQUEST_TIMEOUT = 2000;
    private final List<Neighbour> neighbours;
    private ConcurrentHashMap<Neighbour, Boolean> aliveNodes;
    private ConcurrentHashMap<Neighbour, Neighbour> deadNodeSuccesors;
    private List<Neighbour> deadNodes;
    private Messenger messenger;
    private ConcurrentMap<Integer, Set<Neighbour>> succesors;

    public HeartbeatHandler(Messenger messenger, List<Neighbour> neighbours, ConcurrentMap<Integer, Set<Neighbour>> succesors) {
        this.neighbours = neighbours;
        this.succesors = succesors;
        this.messenger = messenger;
    }
    private final Thread handler = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    deadNodeSuccesors = null;
                    checkNeighbours();
                    Thread.sleep(REQUEST_TIMEOUT);
                    checkDeadNodeSuccesors();
                    aliveNodes = null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeartbeatHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * returns 2 succesors from a node ???? if already in the neighbours
         *
         * @param node
         * @return
         */
        private Neighbour[] getSuccesors(Neighbour node) {
            Neighbour[] successors2 = new Neighbour[2];
            Set<Neighbour> set = succesors.get(node.hashCode());
            int i = 0;
            for (Iterator<Neighbour> it = set.iterator(); it.hasNext() && i < 2;) {
                successors2[i] = it.next();
                i++;
            }
            return successors2;
        }

        private void checkDeadNodeSuccesors() {
            for (Map.Entry<Neighbour, Boolean> entry : aliveNodes.entrySet()) {
                Neighbour node = entry.getKey();
                Boolean alive = entry.getValue();
                if (!alive) {
                    Neighbour[] temp = getSuccesors(node);
                    if (temp[0] != null) {
                        deadNodeSuccesors.put(temp[0], node);
                    }
                    if (temp[1] != null) {
                        deadNodeSuccesors.put(temp[1], node);
                    }
                }
            }
        }

        private void checkNeighbours() {
            ArrayList<Neighbour> neighboursList = (ArrayList<Neighbour>) ((ArrayList<Neighbour>) neighbours).clone();
            aliveNodes = new ConcurrentHashMap<>();
            for (Neighbour neighbour : neighboursList) {
                if (checkNodeHeartbeat(neighbour)) {
                    checkServiceHeartbeat(neighbour);
                    aliveNodes.put(neighbour, Boolean.FALSE);
                }
            }
        }
    };

    public void start() {
        handler.start();
    }

    boolean checkNodeHeartbeat(Neighbour node) {
        try {
            InetAddress address = InetAddress.getByName(node.getIp());
            return address.isReachable(PING_TIMEOUT);
        } catch (IOException e) {
        }
        return false;
    }

    boolean checkServiceHeartbeat(Neighbour node) {
        RequestMessage requestMessage = new IsAliveMessage(node);
        messenger.sendMessage(requestMessage);
        return false;
    }

    /**
     * update heartbeat status. update neighbour list if the sender is a
     * successor of a dead node.
     *
     * @param response
     */
    public void handleIsAliveResponse(AliveMessage response) {
        Neighbour node = new Neighbour(response.getIp(), response.getPort());
        if (aliveNodes != null && aliveNodes.containsKey(node)) {
            aliveNodes.put(node, Boolean.TRUE);
        } else if (deadNodeSuccesors != null && deadNodeSuccesors.contains(node)) {
            //remove deadNode form neighbour list
            //add successor
            //request successors
        }
    }

    public void handleIsAliveRequest(IsAliveMessage request) {
        AliveMessage response = new AliveMessage(request.getIp(), request.getPort());
        messenger.sendMessage(response);
    }
}
