package registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObs extends Remote
{
    /**
     * @param name of the client which shot
     * @param pos destination of the shot
     * @return if shot was valid
     */
    int shoot(String name, int pos) throws RemoteException;

    /**
     *
     * @param name of the client which shot
     * @param startPos destination of the placement
     * @param endPos destination of the placement
     * @return if placement was valid
     */
    int placeShip(String name, int startPos, int endPos) throws RemoteException;

    /**
     * @param name to associate with the client reference
     */
    void clientAdded(String name) throws RemoteException;
}
