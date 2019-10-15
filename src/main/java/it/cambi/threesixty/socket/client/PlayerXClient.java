/**
 * 
 */
package it.cambi.threesixty.socket.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

public class PlayerXClient extends AbstractClient
{

    public PlayerXClient(String IPAddress, int port) throws IOException
    {
        setSocket(new Socket(IPAddress, port));
        setMessages(new LinkedBlockingQueue<SocketDispatcher>());
        setServer(new ConnectionToServer(getSocket()));

        SocketDispatcher dispatcherInitiator = new SocketDispatcher();
        dispatcherInitiator.setPlayerType(PlayersEnum.PLAYERX);
        dispatcherInitiator.setSocket(getSocketString());

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        SocketDispatcher message = getMessages().take();

                        System.out.println("PlayerX says ... Message Received: " + message.getMessage());

                        dispatcherInitiator.setMessage("PlayerX is sending message number ");

                        send(dispatcherInitiator);
                    }
                    catch (InterruptedException | JsonProcessingException e)
                    {
                    }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

}
