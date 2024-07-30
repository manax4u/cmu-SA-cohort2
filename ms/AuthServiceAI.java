import java.rmi.RemoteException;

import java.rmi.*;

public interface AuthServiceAI extends java.rmi.Remote
{
    /*******************************************************
     * Creates a new order from the provided arguments.
     * Returns an OK message or an error string.
     *******************************************************/

    boolean authenticate(String username, String password) throws RemoteException;
    boolean register(String username, String password, String role) throws RemoteException;

}