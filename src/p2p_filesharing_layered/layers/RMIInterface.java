package p2p_filesharing_layered.layers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface  extends Remote{

     public abstract void getMessage(String s,String ip,int port) throws RemoteException;
}
