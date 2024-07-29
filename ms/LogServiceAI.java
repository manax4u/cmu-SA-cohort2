import java.rmi.*;

public interface LogServiceAI extends java.rmi.Remote
{
    void log(String level, String message) throws RemoteException;
}