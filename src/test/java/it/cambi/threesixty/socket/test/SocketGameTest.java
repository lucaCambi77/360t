/**
 * 
 */
package it.cambi.threesixty.socket.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
public class SocketGameTest
{

    public static void main(String[] args) throws IOException, InterruptedException
    {

        new SocketServer(59090);

        CountDownLatch latch = new CountDownLatch(10);

        InitiatorClient initiator = new InitiatorClient("127.0.0.1", 59090, latch);
        new PlayerXClient("127.0.0.1", 59090);

        SocketDispatcher initiatorDispatcher = new SocketDispatcher();
        initiatorDispatcher.setPlayerType(PlayersEnum.INITIATOR);
        initiatorDispatcher.setSocket(initiator.getSocketString());

        initiatorDispatcher.setMessage("Hello from InitiatorClient");

        initiator.send(initiatorDispatcher);

        latch.await();

        System.out.println("Game over");
    }

}
