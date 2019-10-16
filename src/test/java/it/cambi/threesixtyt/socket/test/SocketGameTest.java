/**
 * 
 */
package it.cambi.threesixtyt.socket.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.cambi.threesixtyt.AbstractMain;
import it.cambi.threesixtyt.Main;
import it.cambi.threesixtyt.constant.Constant;
import it.cambi.threesixtyt.socket.client.InitiatorClient;
import it.cambi.threesixtyt.socket.client.PlayerXClient;

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
