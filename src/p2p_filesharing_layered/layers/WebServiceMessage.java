package p2p_filesharing_layered.layers;

import com.google.gson.Gson;

public class WebServiceMessage {

    private String message;
    private String ip;
    private int port;

    public WebServiceMessage(String message, String ip, int port) {
        this.message = message;
        this.port = port;
        this.ip = ip;
    }

    public static WebServiceMessage getMessageObject(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, WebServiceMessage.class);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJsonMessage() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
