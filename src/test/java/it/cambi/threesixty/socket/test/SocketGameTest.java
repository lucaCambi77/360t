/**
 * 
 */
package it.cambi.threesixty.socket.test;

import java.io.IOException;

/**
 * @author luca
 *
 */
public class SocketGameTest
{

    public static void main(String[] args) throws IOException
    {
        new DataServer(59090);

        InitiatorClient initiator = new InitiatorClient("127.0.0.1", 59090);

        PlayerXClient playerX = new PlayerXClient("127.0.0.1", 59090);

        initiator.send("Hello from InitiatorClient");

        initiator.send("Hello from InitiatorClient 2");

        playerX.send("Hello from PlayerXClient");

        playerX.send("Hello from PlayerXClient 2");
    }

}
