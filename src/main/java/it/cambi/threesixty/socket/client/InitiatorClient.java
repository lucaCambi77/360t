/**
 * 
 */
package it.cambi.threesixty.socket.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

public class InitiatorClient extends AbstractClient
{
    private AtomicInteger countDown;
    private CountDownLatch latch;

    public InitiatorClient(String IPAddress, int port, CountDownLatch latchIn, AtomicInteger countDownIn) throws IOException
    {
        setSocket(new Socket(IPAddress, port));
        setMessages(new LinkedBlockingQueue<SocketDispatcher>());
        setServer(new ConnectionToServer(getSocket()));

        this.latch = latchIn;
        this.countDown = countDownIn;

        SocketDispatcher dispatcherInitiator = new SocketDispatcher();
        dispatcherInitiator.setPlayerType(PlayersEnum.INITIATOR);
        dispatcherInitiator.setSocket(getSocketString());

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (countDown.get() > 0)
                {
                    try
                    {
                        SocketDispatcher message = getMessages().take();

                        System.out.println("Initiator says ... Message Received: " + message.getMessage());

                        countDown.decrementAndGet();
                        latch.countDown();

                        Thread.sleep(1000);

                        dispatcherInitiator.setMessage("Initiator is sending message number ");

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
