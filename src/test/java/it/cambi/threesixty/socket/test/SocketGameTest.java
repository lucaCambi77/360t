/**
 * 
 */
package it.cambi.threesixty.socket.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.cambi.threesixty.AbstractMain;
import it.cambi.threesixty.Main;
import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.socket.client.InitiatorClient;
import it.cambi.threesixty.socket.client.PlayerXClient;

/**
 * @author luca
 * 
 *         Test for a socket game, it checks if count down gets to zero after game is finished
 */
public class SocketGameTest extends AbstractMain
{

    private Main main = new Main();

    @Test
    public void testSocketGame() throws Exception
    {

        main.startServer();

        InitiatorClient initiatorClient = new InitiatorClient("127.0.0.1", Constant.socketPort, getLatch(), getCountDown());
        main.play(initiatorClient, new PlayerXClient("127.0.0.1", Constant.socketPort), getLatch());

        assertEquals(0, getCountDown().get());

    }

}
