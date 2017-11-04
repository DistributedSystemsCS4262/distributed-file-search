package p2p_filesharing_layered;

public class Utility {
    public static String getMessageLength(String message){
        String length = "0000";
        int x = message.length() + 5;
        if (x < 10 && x > 0) {
            length = "000" + x;
        }
        if (x >= 10 && x < 100) {
            length = "00" + x;

        }
        if (x >= 100 && x < 999) {
            length = "0" + x;

        }
        if (x >= 1000) {
            length = "" + x;

        }

        return length;
    }
}
