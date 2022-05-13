package client;

import client.gui.GameField;
import registry.ClientObs;
import registry.ServerObs;
import server.Server;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class Client implements ClientObs
{
    private ServerObs server;
    private String regName;
    private boolean playerOnTurn;
    private final GameField gameField;

    public Client(GameField gameField)
    {
        this.gameField = gameField;
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
            server = (ServerObs) registry.lookup("s");

            ClientObs clientObs = (ClientObs) UnicastRemoteObject.exportObject(this, 0);

            if (!Arrays.asList(registry.list()).contains("c1"))
            {
                regName = "c1";
                playerOnTurn = false;
            }
            else if (!Arrays.asList(registry.list()).contains("c2"))
            {
                regName = "c2";
                playerOnTurn = true;
            }
            else
            {
                System.out.println("server full");
                return;
            }

            registry.bind(regName, clientObs);
            server.clientAdded(regName);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean placeShips(Point[] ships)
    {
        try
        {
            return server.placeShips(regName, ships);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param pos destination to shoot
     * @return if shot was hit; 0 = no hit; 1 = hit; 2 = hit + ship destroyed; 3 = hit + all ships destroyed;
     */
    public int shoot(int pos)
    {
        try
        {
            int hit = server.shoot(regName, pos);
            setPlayerOnTurn(hit > 0);
            return hit;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void shot(int pos, boolean onTurn) throws RemoteException
    {
        setPlayerOnTurn(onTurn);
        //Schiff an der Stelle pos wurde abgeschossen todo schiff löschen
    }

    public boolean isPlayerOnTurn()
    {
        return playerOnTurn;
    }

    private void setPlayerOnTurn(boolean onTurn)
    {
        System.out.println(onTurn);
        gameField.colorBorder(onTurn);
        playerOnTurn = onTurn;
    }
}
