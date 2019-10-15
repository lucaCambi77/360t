/**
 * 
 */
package it.cambi.threesixty.socket.client;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.Player;
import it.cambi.threesixty.players.enums.PlayersEnum;
import it.cambi.threesixty.socket.server.SocketServer;

/**
 * @author luca
 * 
 *         PlayerX client instance communicating with {@link InitiatorClient} over a {@link SocketServer}
 * 
 *         Similar to the game based on threads, messages are processes by a BlockingQueue but shared over the network with sockets
 *
 */
public class PlayerXClient extends AbstractClient implements Player
{
    private int sentMessages = 0;

    public PlayerXClient(String IPAddress, int port) throws Exception
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
                        takeMessage();

                        putMessage(dispatcherInitiator);
                    }
                    catch (InterruptedException | JsonProcessingException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

        };

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    @Override
    public <T extends Dispatcher> void putMessage(T dispatcher) throws InterruptedException, JsonProcessingException
    {
        dispatcher.setMessage("PlayerX is sending message number " + ++sentMessages);

        send((SocketDispatcher) dispatcher);

    }

    @Override
    public void takeMessage() throws InterruptedException
    {
        SocketDispatcher message = getMessages().take();

        System.out.println("PlayerX says ... Message Received: " + message.getMessage());

    }

}
