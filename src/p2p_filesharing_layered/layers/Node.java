package p2p_filesharing_layered.layers;

public interface Node {
    void send(String message, String ip, int port);
}
