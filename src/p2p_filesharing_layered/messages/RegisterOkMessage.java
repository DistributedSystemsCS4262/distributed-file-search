package p2p_filesharing_layered.messages;

import p2p_filesharing_layered.domain.Neighbour;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RegisterOkMessage {
    List<Neighbour> neighbours;

    public RegisterOkMessage(StringTokenizer token){
        int no_of_neighbours = Integer.valueOf(token.nextToken());
        ArrayList<Neighbour> new_neighbours = new ArrayList<>();
        // add neighbours
        if (no_of_neighbours > 0 && no_of_neighbours < 9996) {
            while (token.hasMoreTokens()) {
                String ip = token.nextToken();
                String Nport = token.nextToken();
                new_neighbours.add(new Neighbour(ip, Integer.parseInt(Nport)));

                if (new_neighbours.size() == 2) {
                    break;
                }
            }
        }
        this.neighbours = new_neighbours;
    }

    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Neighbour> neighbours) {
        this.neighbours = neighbours;
    }
}
