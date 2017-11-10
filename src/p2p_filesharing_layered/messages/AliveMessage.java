/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing_layered.messages;

import java.util.StringTokenizer;

/**
 *
 * @author Scorpius
 */
public class AliveMessage extends RequestMessage {

    public static final String ACTION = "ALIVE";

    public AliveMessage(String ip, int port) {
        super(ACTION, ip, port);
    }

    public AliveMessage(String action, StringTokenizer stringTokenizer) {
        super(action, stringTokenizer);
    }
}
