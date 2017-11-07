/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing_layered.messages;

import java.util.StringTokenizer;
import p2p_filesharing_layered.domain.Neighbour;

/**
 *
 * @author ChathurangaKCD
 */
public class IsAliveMessage extends RequestMessage {

    public static final String ACTION = "ISALIVE";

    public IsAliveMessage(Neighbour node) {
        super(ACTION, node.getIp(), node.getPort());
    }
    public IsAliveMessage(String action, StringTokenizer stringTokenizer) {
        super(action, stringTokenizer);
    }
}
