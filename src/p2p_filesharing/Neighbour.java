/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_filesharing;

/**
 *
 * @author Scorpius
 */
public class Neighbour {

    private String ip;
    private int port;
    private String name;

    public Neighbour(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }
    public Neighbour(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.name = "";
    }

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }
}
