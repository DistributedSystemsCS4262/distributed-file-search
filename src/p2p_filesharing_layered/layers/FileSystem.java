package p2p_filesharing_layered.layers;

import java.util.*;

public class FileSystem {
    private HashMap<String, ArrayList<String>> fileDictionary;
    private ArrayList<String> fileList = new ArrayList<>(Arrays.asList("Adventures of Tintin", "Jack and Jill", "Glee", "The Vampire Diarie", "King Arthur", "Windows XP", "Harry Potter", "Kung Fu Panda", "Lady Gaga", "Twilight", "Windows 8", "Mission Impossible", "Turn Up The Music", "Super Mario", "American Pickers", "Microsoft Office 2010", "Happy Feet", "Modern Family", "American Idol", "Hacking for Dummies"));
    private List<String> systemFileList;

    public FileSystem(){
        initFilelist();
    }

    private void initFilelist() {
        ArrayList<String> filelistNode = new ArrayList<>();
        Random rn = new Random();
        for(int i=0; i<((fileList.size()/2)+1);i++){
            int ind = rn.nextInt(this.fileList.size());
            if(!filelistNode.contains(this.fileList.get(ind))){
                filelistNode.add(this.fileList.get(ind));
            }
        }
        this.systemFileList = filelistNode;
        this.fileDictionary=generateFileDictionry(filelistNode);
    }

    private HashMap<String, ArrayList<String>> generateFileDictionry(ArrayList<String> filelistNode) {
        HashMap<String, ArrayList<String>> results = new HashMap<>();
        for (String fileName : fileList) {
            StringTokenizer stringTokenizer = new StringTokenizer(fileName);
            while (stringTokenizer.hasMoreTokens()) {
                String word = stringTokenizer.nextToken();
                if (results.containsKey(word)) {
                    results.get(word).add(fileName);
                } else {
                    results.put(word, new ArrayList<>(Arrays.asList((fileName))));
                }
            }
        }
        return results;
    }

    public List<String> searchFiles(String word){
        return fileDictionary.get(word);
    }

    public List<String> getAllFiles(){
        return this.systemFileList;
    }
}
