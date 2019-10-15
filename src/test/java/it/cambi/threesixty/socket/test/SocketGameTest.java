/**
 * 
 */
package it.cambi.threesixty.socket.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import it.cambi.threesixty.AbstractMain;
import it.cambi.threesixty.Main;
import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.socket.client.InitiatorClient;
import it.cambi.threesixty.socket.client.PlayerXClient;

/**
 * @author luca
 *
 */
public class SocketGameTest extends AbstractMain
{

    private Main main = new Main();

    @Test
    public void testSocketGame() throws IOException, InterruptedException
    {

        main.startServer();

        InitiatorClient initiatorClient = new InitiatorClient("127.0.0.1", Constant.socketPort, getLatch(), getCountDown());
        main.play(initiatorClient, new PlayerXClient("127.0.0.1", Constant.socketPort), getLatch());

        assertEquals(0, getCountDown().get());

    }

}
