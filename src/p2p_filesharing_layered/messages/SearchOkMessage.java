package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.Constants;
import p2p_filesharing_layered.Utility;

import java.util.List;

public class SearchOkMessage extends RequestMessage {
    private List<String> fileList;

    public SearchOkMessage(String action, String ip, int port, List<String> fileList) {
        super(action, ip, port);
        this.fileList = fileList;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String packetMessage() {
        String searchOk ="SEROK "+fileList.size()+" "+ Constants.IP_ADDRESS + " " + Constants.PORT+" 1";
        for(String file:fileList){
            searchOk=searchOk+ " " + file;
        }
        searchOk = Utility.getMessageLength(searchOk) + " " + searchOk;
        return searchOk;
    }
}
