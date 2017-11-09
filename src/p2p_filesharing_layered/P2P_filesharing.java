package p2p_filesharing_layered;

import p2p_filesharing_layered.interfaces.Login;
import p2p_filesharing_layered.layers.UDP;

public class P2P_filesharing {
    public static void main(String[] args){
        
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Login().setVisible(true);
//            }
//        });
        
        UDP node = new UDP();
        node.start();
    }
}
