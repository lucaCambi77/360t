/**
 * 
 */
package it.cambi.threesixty.socket.test;

import java.io.IOException;

import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
public class SocketGameTest
{

    public static void main(String[] args) throws IOException
    {

        new SocketServer(59090);

        InitiatorClient initiator = new InitiatorClient("127.0.0.1", 59090);
        new PlayerXClient("127.0.0.1", 59090);

        SocketDispatcher dispatcherInitiator = new SocketDispatcher();
        dispatcherInitiator.setPlayerType(PlayersEnum.INITIATOR);
        dispatcherInitiator.setSocket(initiator.getSocketString());

        dispatcherInitiator.setMessage("Hello from InitiatorClient");

        initiator.send(dispatcherInitiator);

    }

}
